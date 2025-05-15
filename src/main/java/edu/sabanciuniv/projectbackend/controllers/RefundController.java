package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.dto.RefundRequest;
import edu.sabanciuniv.projectbackend.models.Refund;
import edu.sabanciuniv.projectbackend.services.RefundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refunds")
@CrossOrigin(origins = "http://localhost:3000")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @GetMapping
    public List<Refund> getAllRefunds() {
        return refundService.getAllRefunds();
    }

    @GetMapping("/{id}")
    public Refund getRefundById(@PathVariable("id") String refundId) {
        return refundService.getRefundById(refundId);
    }

    @PostMapping
    public Refund createRefund(@RequestBody Refund refund) {
        return refundService.saveRefund(refund);
    }

    @DeleteMapping("/{id}")
    public void deleteRefund(@PathVariable("id") String refundId) {
        refundService.deleteRefund(refundId);
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestRefund(@RequestBody RefundRequest request) {
        try {
            Refund refund = refundService.requestRefund(request);
            return ResponseEntity.ok(refund);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/request-all/{orderId}")
    public ResponseEntity<?> requestRefundForEntireOrder(@PathVariable String orderId, @RequestParam String reason) {
        try {
            List<Refund> refunds = refundService.requestRefundForEntireOrder(orderId, reason);
            return ResponseEntity.ok(refunds);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/eligible/{orderId}/{orderItemId}")
    public ResponseEntity<?> checkRefundEligibility(@PathVariable String orderId, @PathVariable String orderItemId) {
        boolean eligible = refundService.isEligibleForRefund(orderId, orderItemId);
        if (eligible) {
            return ResponseEntity.ok("Bu ürün iade için uygundur.");
        } else {
            return ResponseEntity.badRequest().body("Bu ürün iade için uygun değildir.");
        }
    }

    @GetMapping("/eligible/{orderId}")
    public ResponseEntity<?> checkOrderRefundEligibility(@PathVariable String orderId) {
        boolean eligible = refundService.isOrderEligibleForRefund(orderId);
        if (eligible) {
            return ResponseEntity.ok("Bu sipariş iade için uygundur.");
        } else {
            return ResponseEntity.badRequest().body("Bu sipariş iade için uygun değildir.");
        }
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<?> processRefund(@PathVariable("id") String refundId, @RequestParam String status) {
        try {
            Refund refund = refundService.processRefund(refundId, status);
            return ResponseEntity.ok(refund);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}