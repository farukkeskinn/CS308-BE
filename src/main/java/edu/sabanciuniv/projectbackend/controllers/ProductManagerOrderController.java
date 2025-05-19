package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.models.Address;
import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.services.DeliveryService;
import edu.sabanciuniv.projectbackend.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/product-managers/orders")
public class ProductManagerOrderController {

    private final OrderService orderService;
    private final DeliveryService deliveryService;

    public ProductManagerOrderController(OrderService orderService, DeliveryService deliveryService) {
        this.orderService = orderService;
        this.deliveryService = deliveryService;
    }

    /**
     * PATCH /api/product-managers/orders/{orderId}/status?status=DELIVERED
     *
     * Updates the order’s orderStatus to the given value.
     */
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> changeOrderStatus(
            @PathVariable String orderId,
            @RequestParam("status") String status
    ) {
        Order updated = orderService.updateOrderStatus(orderId, status);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

         if ("IN_TRANSIT".equalsIgnoreCase(status)) {
             String addrId = Optional.ofNullable(updated.getShippingAddress())
                     .map(Address::getAddressId)
                     .orElse(null);
             deliveryService.createForOrder(orderId, addrId);
         }
         else if ("DELIVERED".equalsIgnoreCase(status)) {
             // when it finally delivers, update that Delivery row
             deliveryService.markDeliveredForOrder(orderId);
         }

        return ResponseEntity.ok(updated);
    }
}