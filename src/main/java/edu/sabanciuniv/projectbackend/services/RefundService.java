package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.dto.RefundRequest;
import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.models.OrderItem;
import edu.sabanciuniv.projectbackend.models.Product;
import edu.sabanciuniv.projectbackend.models.Refund;
import edu.sabanciuniv.projectbackend.repositories.RefundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefundService {

    private final RefundRepository refundRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private EmailService emailService;

    // Simülasyon için zamanı tutan değişkenler
    private static LocalDateTime simulationStartTime = LocalDateTime.now();
    private static final double SIMULATION_SPEED = 0.5; // Her saniye 2 gün (1 saniye = 1/0.5 = 2 gün)

    public RefundService(RefundRepository refundRepository) {
        this.refundRepository = refundRepository;
    }

    public List<Refund> getAllRefunds() {
        return refundRepository.findAll();
    }

    public Refund getRefundById(String refundId) {
        return refundRepository.findById(refundId).orElse(null);
    }

    @Transactional
    public Refund saveRefund(Refund refund) {
        return refundRepository.save(refund);
    }

    @Transactional
    public void deleteRefund(String refundId) {
        refundRepository.deleteById(refundId);
    }

    /**
     * Simüle edilmiş zamanı hesaplar (her 2 saniyede 1 gün)
     */
    public LocalDateTime getSimulatedTime() {
        Duration duration = Duration.between(simulationStartTime, LocalDateTime.now());
        long secondsElapsed = duration.getSeconds();
        return simulationStartTime.plusDays((long)(secondsElapsed * SIMULATION_SPEED));
    }

    /**
     * Ürünün iade uygunluğunu kontrol eder
     */
    public boolean isEligibleForRefund(String orderId, String orderItemId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) return false;

        // Sipariş DELIVERED durumunda mı?
        if (!"DELIVERED".equals(order.getOrderStatus()))
            return false;

        // Sipariş öğesi var mı?
        boolean orderItemExists = order.getOrderItems().stream()
                .anyMatch(item -> item.getOrderItemId().equals(orderItemId));
        if (!orderItemExists) return false;

        // 30 günlük süre kontrolü
        LocalDateTime orderDate = order.getOrderDate();
        LocalDateTime thirtyDaysAfterOrder = orderDate.plusDays(30);
        if (LocalDateTime.now().isAfter(thirtyDaysAfterOrder)) {
            return false;
        }

        return true;
    }

    /**
     * Siparişin tümünün iade uygunluğunu kontrol eder
     */
    public boolean isOrderEligibleForRefund(String orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) return false;

        // Sipariş DELIVERED durumunda mı?
        if (!"DELIVERED".equals(order.getOrderStatus()))
            return false;

        // Siparişte iade edilebilecek ürün var mı?
        if (order.getOrderItems().isEmpty()) return false;

        // 30 günlük süre kontrolü
        LocalDateTime orderDate = order.getOrderDate();
        LocalDateTime thirtyDaysAfterOrder = orderDate.plusDays(30);
        if (LocalDateTime.now().isAfter(thirtyDaysAfterOrder)) {
            return false;
        }

        return true;
    }

    @Transactional
    public List<Refund> requestRefundForEntireOrder(String orderId, String reason) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Sipariş bulunamadı");
        }

        if (!"DELIVERED".equals(order.getOrderStatus())) {
            throw new IllegalArgumentException("Sadece teslim edilmiş siparişler iade edilebilir");
        }

        LocalDateTime orderDate = order.getOrderDate();
        LocalDateTime thirtyDaysAfterOrder = orderDate.plusDays(30);
        if (LocalDateTime.now().isAfter(thirtyDaysAfterOrder)) {
            throw new IllegalArgumentException("İade süresi dolmuştur. Siparişler 30 gün içinde iade edilebilir");
        }

        if (order.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("Siparişte iade edilecek ürün bulunamadı");
        }

        List<Refund> refunds = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (OrderItem orderItem : new ArrayList<>(order.getOrderItems())) {
            Refund refund = new Refund();
            refund.setRefundId(UUID.randomUUID().toString());
            refund.setOrder(order);
            refund.setOrderItem(orderItem);
            refund.setRequestDate(now);
            refund.setRefundStatus("PENDING");
            refund.setRefundAmount(orderItem.getPriceAtPurchase() * orderItem.getQuantity());
            refund.setReason(reason);

            refunds.add(saveRefund(refund));
        }

        // Siparişin durumunu güncelle
        order.setOrderStatus("REFUNDED");
        orderService.saveOrder(order);

        return refunds;
    }

    /**
     * İade işlemini tamamla (Admin veya otomatik işlem için)
     */
    @Transactional
    public Refund processRefund(String refundId, String status) {
        Refund refund = getRefundById(refundId);
        if (refund == null) {
            throw new IllegalArgumentException("İade kaydı bulunamadı");
        }

        refund.setRefundStatus(status);
        refund.setProcessDate(LocalDateTime.now());
        
        Refund savedRefund = saveRefund(refund);

        // İade işlemi onaylandığında e-posta gönder
        if ("APPROVED".equals(status)) {
            emailService.sendRefundConfirmationEmail(savedRefund);
        }

        return savedRefund;
    }
}