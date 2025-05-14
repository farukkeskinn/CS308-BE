package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.dto.OrderItemDTO;
import edu.sabanciuniv.projectbackend.dto.OrderSummaryDTO;
import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.models.OrderItem;
import edu.sabanciuniv.projectbackend.models.ShoppingCart;
import edu.sabanciuniv.projectbackend.repositories.OrderRepository;
import edu.sabanciuniv.projectbackend.utils.EncryptionUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final EncryptionUtil encryptionUtil;
    public OrderService(OrderRepository orderRepository, EncryptionUtil encryptionUtil) {
        this.orderRepository = orderRepository;
        this.encryptionUtil = encryptionUtil;
    }

    /** Tüm siparişler */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /** Tek bir sipariş */
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    /** Müşteriye ait siparişler */
    public List<Order> getOrdersByCustomer(String customerId) {
        return orderRepository.findByCustomer_CustomerId(customerId);
    }

    /** Yeni veya güncelleme */
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    /** Silme */
    public void deleteOrder(String orderId) {
        orderRepository.deleteById(orderId);
    }

    /** Cart’tan Order oluşturma (checkout akışı) */
    public Order createOrderFromCart(ShoppingCart cart) {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setCustomer(cart.getCustomer());
        // Başlangıç statüsünü “PROCESSING” yapıyoruz
        order.setOrderStatus("PROCESSING");
        order.setPaymentStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());

        double totalPrice = cart.getShoppingCartItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        order.setTotalPrice(totalPrice);

        List<OrderItem> orderItems = cart.getShoppingCartItems().stream().map(cartItem -> {
            OrderItem oi = new OrderItem();
            oi.setOrderItemId(UUID.randomUUID().toString());
            oi.setOrder(order);
            oi.setProduct(cartItem.getProduct());
            oi.setQuantity(cartItem.getQuantity());
            oi.setPriceAtPurchase(cartItem.getProduct().getPrice());
            return oi;
        }).collect(Collectors.toList());
        order.setOrderItems(orderItems);

        return orderRepository.save(order);
    }

    /**
     * Siparişi iptal etme. Sadece PROCESSING statüsündeyse geçerli.
     * @return true ise iptal edildi, false ise iptal edilemedi.
     */
    public boolean cancelOrder(String orderId) {
        Optional<Order> opt = orderRepository.findById(orderId);
        if (opt.isEmpty()) return false;
        Order o = opt.get();
        if ("PROCESSING".equals(o.getOrderStatus())) {
            o.setOrderStatus("CANCELLED");
            orderRepository.save(o);
            return true;
        }
        return false;
    }

    /**
     * Manuel statü güncelleme (admin / test amaçlı)
     * @return güncellenmiş Order veya null
     */
    public Order updateOrderStatus(String orderId, String status) {
        Optional<Order> opt = orderRepository.findById(orderId);
        if (opt.isEmpty()) return null;
        Order o = opt.get();
        o.setOrderStatus(status);
        return orderRepository.save(o);
    }

    public OrderSummaryDTO toSummaryDto(Order o) {
        return new OrderSummaryDTO(
                o.getOrderId(),
                o.getTotalPrice(),
                o.getOrderDate(),
                o.getOrderStatus(),
                o.getPaymentStatus(),
                o.getOrderItems().stream()
                        .map(oi -> new OrderItemDTO(
                                oi.getProduct().getProductId(),
                                oi.getProduct().getName(),
                                oi.getPriceAtPurchase(),
                                oi.getQuantity()))
                        .collect(Collectors.toList()),
                safeInvoiceLink(o.getInvoiceLink())
        );
    }

    private String safeInvoiceLink(String raw) {
        if (raw == null) return null;
        try {
            // V2 – AES‑GCM
            return encryptionUtil.decryptString(raw);
        } catch (Exception v2ex) {
            try {
                // V1 – eski ECB
                return EncryptionUtil.decryptLegacy(raw);
            } catch (Exception v1ex) {
                // Çözülemedi → ham hâliyle döndür (çok istisnai)
                return raw;
            }
        }
    }
}
