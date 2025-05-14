package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.*;
import edu.sabanciuniv.projectbackend.repositories.ProductRepository;
import edu.sabanciuniv.projectbackend.repositories.RefundRepository;
import edu.sabanciuniv.projectbackend.repositories.SalesManagerRepository;
import edu.sabanciuniv.projectbackend.repositories.WishlistItemRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SalesManagerService {

    private final SalesManagerRepository salesManagerRepository;
    private final ProductRepository productRepository;
    private final RefundRepository refundRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final EmailService emailService;

    public SalesManagerService(SalesManagerRepository salesManagerRepository,
                               ProductRepository productRepository,
                               RefundRepository refundRepository,
                               WishlistItemRepository wishlistItemRepository,
                               EmailService emailService) {
        this.salesManagerRepository = salesManagerRepository;
        this.productRepository = productRepository;
        this.refundRepository = refundRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.emailService = emailService;
    }

    public List<SalesManager> getAllSalesManagers() {
        return salesManagerRepository.findAll();
    }

    public SalesManager getSalesManager(String smId) {
        return salesManagerRepository.findById(smId).orElse(null);
    }

    public SalesManager saveSalesManager(SalesManager salesManager) {
        return salesManagerRepository.save(salesManager);
    }

    public void deleteSalesManager(String smId) {
        salesManagerRepository.deleteById(smId);
    }

    public List<Order> getAllOrders() {
        return salesManagerRepository.findAllOrders();
    }

    public Double calculateTotalRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = salesManagerRepository.findOrdersByDateRange(startDate, endDate);

        return orders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }

    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return salesManagerRepository.findOrdersByDateRange(startDate, endDate);
    }

    public Map<String, Object> getInvoiceInfoInDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = getOrdersByDateRange(startDate, endDate);

        List<Map<String, Object>> invoiceList = new ArrayList<>();
        for (Order order : orders) {
            String invoiceFileName = "invoice_" + order.getOrderId() + ".pdf";
            File invoiceFile = new File("build/resources/main/static/invoices", invoiceFileName);

            if (invoiceFile.exists()) {
                Map<String, Object> invoiceInfo = new HashMap<>();
                invoiceInfo.put("orderId", order.getOrderId());
                invoiceInfo.put("orderDate", order.getOrderDate());
                invoiceInfo.put("customerName", order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
                invoiceInfo.put("totalAmount", order.getTotalPrice());
                invoiceInfo.put("invoiceUrl", "http://localhost:8080/invoices/" + invoiceFileName);

                invoiceList.add(invoiceInfo);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("invoices", invoiceList);
        result.put("count", invoiceList.size());
        result.put("startDate", startDate);
        result.put("endDate", endDate);

        return result;
    }

    public Map<String, Object> applyProductDiscount(String productId, Integer discountPercentage) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (!productOpt.isPresent()) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }

        Product product = productOpt.get();

        Double originalPrice = product.getPrice();
        Double discountAmount = originalPrice * (discountPercentage / 100.0);
        Double discountedPrice = originalPrice - discountAmount;

        product.setDiscounted(true);
        product.setDiscountPercentage(discountPercentage);
        product.setDiscountedPrice(discountedPrice);

        Product savedProduct = productRepository.save(product);

        List<Customer> customers = wishlistItemRepository.findCustomersByProduct(productId);

        customers.forEach(c -> emailService.sendDiscountMail(c.getEmail(), product));

        Map<String, Object> result = new HashMap<>();
        result.put("productId", productId);
        result.put("name", product.getName());
        result.put("originalPrice", originalPrice);
        result.put("discountPercentage", discountPercentage);
        result.put("discountAmount", discountAmount);
        result.put("discountedPrice", discountedPrice);
        result.put("isDiscounted", true);
        result.put("notifiedCount", customers.size());

        return result;
    }

    public List<Refund> getPendingRefunds() {
        return refundRepository.findPendingRefunds();
    }

    public List<Refund> getAllRefunds() {
        return refundRepository.findAll();
    }

    public Refund processRefund(String refundId, String decision, String comment) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new RuntimeException("Refund not found with ID: " + refundId));

        if (!"PENDING".equals(refund.getRefundStatus())) {
            throw new IllegalStateException("This refund has already been processed. Current status: " + refund.getRefundStatus());
        }

        if ("APPROVE".equals(decision)) {
            refund.setRefundStatus("APPROVED");
            OrderItem orderItem = refund.getOrderItem();
            Product product = orderItem.getProduct();
            Integer currentStock = product.getStock();
            product.setStock(currentStock + orderItem.getQuantity());
            Order order = refund.getOrder();

        } else if ("REJECT".equals(decision)) {
            refund.setRefundStatus("REJECTED");
        } else {
            throw new IllegalArgumentException("Invalid decision. Must be either 'APPROVE' or 'REJECT'.");
        }

        return refundRepository.save(refund);
    }

    public Map<String, Object> calculateProfitLossByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = salesManagerRepository.findOrdersByDateRange(startDate, endDate);

        double totalRevenue = orders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();

        double totalCostOfGoodsSold = orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .mapToDouble(item -> {
                    Product product = item.getProduct();
                    double costPrice = product.getCost() != null ? product.getCost() : 0.0;
                    return costPrice * item.getQuantity();
                })
                .sum();

        List<Refund> refunds = refundRepository.findProcessedRefundsByDateRange(startDate, endDate);
        double totalRefunds = refunds.stream()
                .filter(refund -> "APPROVED".equals(refund.getRefundStatus()))
                .mapToDouble(refund -> refund.getOrderItem().getProduct().getPrice() * refund.getOrderItem().getQuantity())
                .sum();

        double grossProfit = totalRevenue - totalCostOfGoodsSold - totalRefunds;

        Map<String, Object> result = new HashMap<>();
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("totalRevenue", totalRevenue);
        result.put("totalCostOfGoodsSold", totalCostOfGoodsSold);
        result.put("totalRefunds", totalRefunds);
        result.put("grossProfit", grossProfit);
        result.put("orderCount", orders.size());
        result.put("refundCount", refunds.size());

        return result;
    }

    public Map<String, Object> setProductPrice(String productId, Double price, Boolean publishProduct) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (!productOpt.isPresent()) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }

        Product product = productOpt.get();

        // Set the price
        product.setPrice(price);

        // Calculate cost if it's not already set (optional, based on your business logic)
        if (product.getCost() == null) {
            // Example: Set cost as 70% of price by default
            product.setCost(price * 0.7);
        }

        // Set the product as visible on the website if publishProduct is true
        if (publishProduct) {
            product.setPublished(true);
        }

        Product savedProduct = productRepository.save(product);

        Map<String, Object> result = new HashMap<>();
        result.put("productId", productId);
        result.put("name", product.getName());
        result.put("price", price);
        result.put("cost", product.getCost());
        result.put("published", product.getPublished());

        return result;
    }

    public List<Product> getUnpublishedProducts() {
        return productRepository.findByPublished(false);
    }
}
