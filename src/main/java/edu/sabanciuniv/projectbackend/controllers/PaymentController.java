package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.models.Payment;
import edu.sabanciuniv.projectbackend.services.PaymentService;
import edu.sabanciuniv.projectbackend.utils.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import edu.sabanciuniv.projectbackend.dto.PaymentRequest;
import edu.sabanciuniv.projectbackend.dto.InvoiceResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.security.Principal;

import jakarta.validation.Valid;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public Payment getPaymentById(@PathVariable("id") String paymentId) {
        return paymentService.getPaymentById(paymentId);
    }

    @PostMapping
    public Payment createPayment(@RequestBody Payment payment) {
        return paymentService.savePayment(payment);
    }

    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable("id") String paymentId) {
        paymentService.deletePayment(paymentId);
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> checkout(
            @Valid @RequestBody PaymentRequest request,
            @RequestHeader("Authorization") String authHeader,
            HttpServletRequest servletRequest
    ) {
        try {
            // 🔐 Token'ı al ve "Bearer " kısmını temizle
            if (!authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token format!");
            }
            String token = authHeader.substring(7); // "Bearer " sonrası

            // 📩 Token'dan email (username) çek
            String username = JWTUtil.getEmailFromToken(token);

            // 🧾 Checkout işlemi başlat
            InvoiceResponse invoice = paymentService.processCheckout(request, username, servletRequest);
            return ResponseEntity.ok(invoice);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Checkout failed: " + e.getMessage());
        }
    }


}
