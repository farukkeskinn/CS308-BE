package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.dto.OrderSummaryDTO;
import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.services.OrderService;
import edu.sabanciuniv.projectbackend.utils.EncryptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
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

    @GetMapping("/{id}/invoice-url")
    public ResponseEntity<?> getDecryptedInvoiceUrl(@PathVariable("id") String orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null || order.getInvoiceLink() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            String decrypted = encryptionUtil.decryptString(order.getInvoiceLink());
            return ResponseEntity.ok(Map.of("url", decrypted));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to decrypt invoice URL."));
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable("id") String orderId) {
        try {
            boolean cancelled = orderService.cancelOrder(orderId);
            if (cancelled) {
                return ResponseEntity.ok().body("Sipariş başarıyla iptal edildi.");
            } else {
                return ResponseEntity.badRequest().body("Sipariş iptal edilemedi. Sadece 'PROCESSING' durumundaki siparişler iptal edilebilir.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/cancel-item/{itemId}")
    public ResponseEntity<?> cancelOrderItem(@PathVariable("id") String orderId,
                                             @PathVariable("itemId") String orderItemId) {
        try {
            boolean cancelled = orderService.cancelOrderItem(orderId, orderItemId);
            if (cancelled) {
                return ResponseEntity.ok().body("Sipariş kalemi başarıyla iptal edildi.");
            } else {
                return ResponseEntity.badRequest().body("Sipariş kalemi iptal edilemedi. Sadece 'PROCESSING' durumundaki siparişlerde iptal işlemi yapılabilir.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}