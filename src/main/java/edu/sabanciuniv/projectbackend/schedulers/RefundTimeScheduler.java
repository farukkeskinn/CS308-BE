package edu.sabanciuniv.projectbackend.schedulers;

import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.repositories.OrderRepository;
import edu.sabanciuniv.projectbackend.services.RefundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RefundTimeScheduler {

    private static final Logger logger = LoggerFactory.getLogger(RefundTimeScheduler.class);

    // Siparişlerin DELIVERED durumuna geçiş zamanlarını tutacak harita
    private final Map<String, LocalDateTime> deliveredTimeMap = new HashMap<>();

    private final OrderRepository orderRepository;
    private final RefundService refundService;

    public RefundTimeScheduler(OrderRepository orderRepository, RefundService refundService) {
        this.orderRepository = orderRepository;
        this.refundService = refundService;
    }

    /**
     * Her dakikada bir çalışır ve teslim edilmiş siparişler için
     * DELIVERED olduktan sonra 30 günlük iade süresini kontrol eder.
     */
    @Scheduled(fixedRate = 60_000)
    public void checkRefundEligibility() {
        logger.info("▶︎ Refund Scheduler çalıştı, sipariş iade edilebilirliği kontrol ediliyor...");

        // Simüle edilmiş zamanı al
        LocalDateTime simulatedNow = refundService.getSimulatedTime();

        // Sadece DELIVERED durumundaki siparişleri al
        List<Order> deliveredOrders = orderRepository.findByOrderStatus("DELIVERED");

        for (Order order : deliveredOrders) {
            String orderId = order.getOrderId();

            // Eğer bu sipariş ilk kez DELIVERED durumunda görülüyorsa, zamanı kaydet
            if (!deliveredTimeMap.containsKey(orderId)) {
                deliveredTimeMap.put(orderId, simulatedNow);
                logger.info("→ Order {} için iade süresi başladı (DELIVERED durumuna geçiş zamanı: {})",
                        orderId, simulatedNow);
                continue; // Bu siparişi atla, 30 gün süre henüz başladı
            }

            // Sipariş DELIVERED durumuna geçtiği zamanı al
            LocalDateTime deliveredTime = deliveredTimeMap.get(orderId);
            LocalDateTime thirtyDaysAfterDelivery = deliveredTime.plusDays(30);

            // Sipariş DELIVERED olduktan 30 gün geçmişse ve hala iade edilebilirse
            if (simulatedNow.isAfter(thirtyDaysAfterDelivery) && order.getRefundable()) {
                order.setRefundable(false);
                orderRepository.save(order);
                logger.info("→ Order {} için iade süresi doldu (DELIVERED durumuna geçtikten 30 gün sonra, simülasyon tarihi: {})",
                        orderId, simulatedNow);
            }
        }
    }
}