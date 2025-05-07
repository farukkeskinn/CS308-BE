package edu.sabanciuniv.projectbackend.schedulers;

import edu.sabanciuniv.projectbackend.models.Delivery;
import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.repositories.DeliveryRepository;
import edu.sabanciuniv.projectbackend.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderStatusScheduler {

    private static final Logger logger = LoggerFactory.getLogger(OrderStatusScheduler.class);

    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;

    public OrderStatusScheduler(OrderRepository orderRepository,
                                DeliveryRepository deliveryRepository) {
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
    }

    /**
     * Her 30 saniyede bir:
     *   PROCESSING → IN_TRANSIT → DELIVERED
     */
    @Scheduled(fixedRate = 60_000)
    public void advanceStatuses() {
        logger.info("▶︎ Scheduler çalıştı, sipariş durumları güncelleniyor...");
        List<Order> orders = orderRepository.findAll();
        for (Order o : orders) {
            String current = o.getOrderStatus();
            switch (current) {
                case "PROCESSING":
                    o.setOrderStatus("IN_TRANSIT");
                    orderRepository.save(o);
                    logger.info("→ Order {} durumu PROCESSING→IN_TRANSIT olarak güncellendi", o.getOrderId());

                    Delivery d = new Delivery();
                    d.setDeliveryId(UUID.randomUUID().toString());
                    d.setOrder(o);
                    d.setDeliveryStatus("IN_TRANSIT");
                    deliveryRepository.save(d);
                    logger.info("   Delivery kaydı oluşturuldu: {}", d.getDeliveryId());
                    break;

                case "IN_TRANSIT":
                    o.setOrderStatus("DELIVERED");
                    orderRepository.save(o);
                    logger.info("→ Order {} durumu IN_TRANSIT→DELIVERED olarak güncellendi", o.getOrderId());
                    break;

                default:
                    // PROCESSING veya IN_TRANSIT değilse atla
            }
        }
    }
}