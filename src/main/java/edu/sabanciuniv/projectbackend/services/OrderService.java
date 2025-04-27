package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.models.OrderItem;
import edu.sabanciuniv.projectbackend.models.ShoppingCart;
import edu.sabanciuniv.projectbackend.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
}
