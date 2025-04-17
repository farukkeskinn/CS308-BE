package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import edu.sabanciuniv.projectbackend.models.ShoppingCart;
import edu.sabanciuniv.projectbackend.models.OrderItem;
import java.util.UUID;
import java.time.LocalDateTime;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(String orderId) {
        orderRepository.deleteById(orderId);
    }

    public Order createOrderFromCart(ShoppingCart cart) {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setCustomer(cart.getCustomer());
        order.setOrderStatus("CREATED");
        order.setPaymentStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());

        // Toplam fiyatı hesapla
        double totalPrice = cart.getShoppingCartItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        order.setTotalPrice(totalPrice);

        // Her bir cart item'i OrderItem olarak ekle
        List<OrderItem> orderItems = cart.getShoppingCartItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemId(UUID.randomUUID().toString()); // ID assignment
            orderItem.setOrder(order);  // ilişkiyi kur
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getProduct().getPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);

        return orderRepository.save(order);
    }

}
