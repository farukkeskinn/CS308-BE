package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.dto.OrderItemDTO;
import edu.sabanciuniv.projectbackend.dto.OrderSummaryDTO;
import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.services.OrderService;
import edu.sabanciuniv.projectbackend.utils.EncryptionUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    private final OrderService orderService;
    private final EncryptionUtil encryptionUtil;

    public OrderController(OrderService orderService, EncryptionUtil encryptionUtil) {
        this.orderService = orderService;
        this.encryptionUtil = encryptionUtil;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderSummaryDTO> getOrderById(@PathVariable String id) {
        Order o = orderService.getOrderById(id);
        return (o == null)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(orderService.toSummaryDto(o));   // ✔
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

        List<OrderSummaryDTO> result = orderService.getOrdersByCustomer(customerId)
                .stream()
                .map(orderService::toSummaryDto)      // ✔ tek satırda dönüşüm
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }




}