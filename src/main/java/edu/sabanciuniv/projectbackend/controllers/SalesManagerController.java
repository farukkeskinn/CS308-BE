package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.models.Product;
import edu.sabanciuniv.projectbackend.models.Refund;
import edu.sabanciuniv.projectbackend.models.SalesManager;
import edu.sabanciuniv.projectbackend.services.SalesManagerService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales-managers")
@CrossOrigin(origins = "http://localhost:3000")
public class SalesManagerController {


    private final SalesManagerService salesManagerService;

    public SalesManagerController(SalesManagerService salesManagerService) {
        this.salesManagerService = salesManagerService;
    }

    @GetMapping
    public List<SalesManager> getAllSalesManagers() {
        return salesManagerService.getAllSalesManagers();
    }

    @GetMapping("/{id}")
    public SalesManager getSalesManagerById(@PathVariable("id") String smId) {
        return salesManagerService.getSalesManager(smId);
    }

    @PostMapping
    public SalesManager createSalesManager(@RequestBody SalesManager salesManager) {
        return salesManagerService.saveSalesManager(salesManager);
    }

    @DeleteMapping("/{id}")
    public void deleteSalesManager(@PathVariable("id") String smId) {
        salesManagerService.deleteSalesManager(smId);
    }

    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return salesManagerService.getAllOrders();
    }

    @GetMapping("/revenue")
    public Map<String, Object> calculateTotalRevenue(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        Double revenue = salesManagerService.calculateTotalRevenueByDateRange(startDate, endDate);

        Map<String, Object> response = new HashMap<>();
        response.put("startDate", startDate);
        response.put("endDate", endDate);
        response.put("totalRevenue", revenue);

        return response;
    }

    @GetMapping("/invoices/dates")
    public ResponseEntity<?> getInvoicesInDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            Map<String, Object> invoices = salesManagerService.getInvoiceInfoInDateRange(startDate, endDate);
            return ResponseEntity.ok(invoices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/products/discount")
    public ResponseEntity<?> applyProductDiscount(
            @RequestParam("productId") String productId,
            @RequestParam("discountPercentage") Integer discountPercentage) {

        try {
            // Validate discount range
            if (discountPercentage < 1 || discountPercentage > 100) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Discount percentage must be between 1 and 100"));
            }

            Map<String, Object> result = salesManagerService.applyProductDiscount(
                    productId, discountPercentage);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/refunds/pending")
    public ResponseEntity<List<Refund>> getPendingRefunds() {
        List<Refund> pendingRefunds = salesManagerService.getPendingRefunds();
        return ResponseEntity.ok(pendingRefunds);
    }

    @GetMapping("/refunds")
    public ResponseEntity<List<Refund>> getAllRefunds() {
        List<Refund> allRefunds = salesManagerService.getAllRefunds();
        return ResponseEntity.ok(allRefunds);
    }

    @PostMapping("/refunds/{refundId}/process")
    public ResponseEntity<?> processRefund(
            @PathVariable("refundId") String refundId,
            @RequestParam("decision") String decision,
            @RequestParam(value = "comment", required = false) String comment) {

        try {
            if (!"APPROVE".equals(decision) && !"REJECT".equals(decision)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Decision must be either 'APPROVE' or 'REJECT'"));
            }

            Refund processedRefund = salesManagerService.processRefund(refundId, decision, comment);
            return ResponseEntity.ok(processedRefund);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/profit-loss")
    public ResponseEntity<?> calculateProfitLoss(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        try {
            Map<String, Object> profitLossData = salesManagerService.calculateProfitLossByDateRange(startDate, endDate);
            return ResponseEntity.ok(profitLossData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/products/set-price")
    public ResponseEntity<?> setProductPrice(
            @RequestParam("productId") String productId,
            @RequestParam("price") Double price,
            @RequestParam(value = "publishProduct", defaultValue = "false") Boolean publishProduct) {

        try {
            // Validate price
            if (price <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Price must be greater than zero"));
            }

            Map<String, Object> result = salesManagerService.setProductPrice(
                    productId, price, publishProduct);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/products/unpublished")
    public ResponseEntity<List<Product>> getUnpublishedProducts() {
        try {
            List<Product> unpublishedProducts = salesManagerService.getUnpublishedProducts();
            return ResponseEntity.ok(unpublishedProducts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
