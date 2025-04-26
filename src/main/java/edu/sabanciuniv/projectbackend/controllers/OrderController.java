package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.dto.OrderItemDTO;
import edu.sabanciuniv.projectbackend.dto.OrderSummaryDTO;
import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderSummaryDTO> getOrderById(@PathVariable("id") String orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) return ResponseEntity.notFound().build();

        OrderSummaryDTO dto = new OrderSummaryDTO(
                order.getOrderId(),
                order.getTotalPrice(),
                order.getOrderDate(),
                order.getOrderStatus(),
                order.getPaymentStatus(),
                order.getOrderItems().stream().map(item -> new OrderItemDTO(
                        item.getProduct().getProductId(),
                        item.getProduct().getName(),
                        item.getPriceAtPurchase(),
                        item.getQuantity()
                )).collect(Collectors.toList()),
                order.getInvoiceLink()
        );

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.saveOrder(order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable("id") String orderId) {
        orderService.deleteOrder(orderId);
    }

    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<List<OrderSummaryDTO>> getByCustomer(@PathVariable String customerId) {
        List<Order> orders = orderService.getOrdersByCustomer(customerId);
        List<OrderSummaryDTO> result = orders.stream()
                .map(order -> new OrderSummaryDTO(
                        order.getOrderId(),
                        order.getTotalPrice(),
                        order.getOrderDate(),
                        order.getOrderStatus(),
                        order.getPaymentStatus(),
                        order.getOrderItems().stream().map(item -> new OrderItemDTO(
                                item.getProduct().getProductId(),
                                item.getProduct().getName(),
                                item.getPriceAtPurchase(),
                                item.getQuantity()
                        )).collect(Collectors.toList()),
                        order.getInvoiceLink()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

}