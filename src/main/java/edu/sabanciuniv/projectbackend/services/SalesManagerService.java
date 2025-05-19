package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.*;
import edu.sabanciuniv.projectbackend.repositories.ProductRepository;
import edu.sabanciuniv.projectbackend.repositories.RefundRepository;
import edu.sabanciuniv.projectbackend.repositories.SalesManagerRepository;
import edu.sabanciuniv.projectbackend.repositories.WishlistItemRepository;
import edu.sabanciuniv.projectbackend.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
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
    private final OrderService orderService;

    @Autowired
    private EncryptionUtil encryptionUtil;

    public SalesManagerService(SalesManagerRepository salesManagerRepository,
                               ProductRepository productRepository,
                               RefundRepository refundRepository,
                               WishlistItemRepository wishlistItemRepository,
                               EmailService emailService,
                               OrderService orderService) {
        this.salesManagerRepository = salesManagerRepository;
        this.productRepository = productRepository;
        this.refundRepository = refundRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.emailService = emailService;
        this.orderService = orderService;
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
            String encryptedInvoiceLink = order.getInvoiceLink();

            if (encryptedInvoiceLink != null && !encryptedInvoiceLink.isBlank()) {
                try {
                    // 🔓 AES-256-GCM decrypt
                    String decryptedLink = encryptionUtil.decryptString(encryptedInvoiceLink);

                    Map<String, Object> invoiceInfo = new HashMap<>();
                    invoiceInfo.put("orderId", order.getOrderId());
                    invoiceInfo.put("orderDate", order.getOrderDate());
                    invoiceInfo.put("customerName", order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
                    invoiceInfo.put("totalAmount", order.getTotalPrice());
                    invoiceInfo.put("invoiceUrl", decryptedLink);
                    invoiceList.add(invoiceInfo);
                } catch (Exception e) {
                    System.err.println("❌ Invoice decrypt failed: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("invoices", invoiceList);
        result.put("count", invoiceList.size());
        result.put("startDate", startDate);
        result.put("endDate", endDate);

        return result;
    }

    @Transactional
    public Map<String, Object> applyProductDiscount(String productId, Integer discountPercentage) {
        try {
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                throw new RuntimeException("Product not found with ID: " + productId);
            }

            Product product = productOpt.get();
            Double originalPrice = product.getPrice();
            Double discountAmount = originalPrice * (discountPercentage / 100.0);
            Double discountedPrice = originalPrice - discountAmount;

            product.setDiscounted(true);
            product.setDiscountPercentage(discountPercentage);
            product.setDiscountedPrice(discountedPrice);

            productRepository.save(product);

            List<Customer> customers = wishlistItemRepository.findCustomersByProduct(productId);
            customers.forEach(c -> emailService.sendDiscountMail(c.getEmail(), product));

            Map<String, Object> result = new HashMap<>();
            result.put("productId", productId);
            result.put("discountedPrice", discountedPrice);
            return result;
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new RuntimeException("Product was updated by another user. Please refresh and try again.");
        }
    }

    public List<Refund> getPendingRefunds() {
        return refundRepository.findPendingRefunds();
    }

    public List<Refund> getAllRefunds() {
        return refundRepository.findAll();
    }

    @Transactional
    public Refund processRefund(String refundId, String decision, String comment) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new RuntimeException("Refund not found with ID: " + refundId));

        if (!"PENDING".equals(refund.getRefundStatus())) {
            throw new IllegalStateException("This refund has already been processed. Current status: " + refund.getRefundStatus());
        }

        // İşlem tarihini her durumda set et
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Setting process date to: " + now);
        refund.setProcessDate(now);

        if ("APPROVE".equals(decision)) {
            refund.setRefundStatus("APPROVED");
            OrderItem orderItem = refund.getOrderItem();
            Product product = orderItem.getProduct();
            Integer currentStock = product.getStock();
            product.setStock(currentStock + orderItem.getQuantity());
            Order order = refund.getOrder();
            order.setOrderStatus("REFUNDED");
            orderService.saveOrder(order);
            
            // Önce refund'ı kaydet
            Refund savedRefund = refundRepository.save(refund);
            
            // processDate'in null olmadığından emin ol
            if (savedRefund.getProcessDate() == null) {
                savedRefund.setProcessDate(now);
                savedRefund = refundRepository.save(savedRefund);
            }
            
            System.out.println("Saved refund with process date: " + savedRefund.getProcessDate());
            
            // Sonra e-posta gönder
            try {
                System.out.println("=== Refund Email Debug Info ===");
                System.out.println("Refund ID: " + savedRefund.getRefundId());
                System.out.println("Process Date: " + savedRefund.getProcessDate());
                System.out.println("Customer Email: " + savedRefund.getOrder().getCustomer().getEmail());
                System.out.println("Customer Name: " + savedRefund.getOrder().getCustomer().getFirstName());
                System.out.println("Order ID: " + savedRefund.getOrder().getOrderId());
                System.out.println("Refund Amount: " + savedRefund.getRefundAmount());
                System.out.println("Refund Reason: " + savedRefund.getReason());
                System.out.println("============================");
                
                emailService.sendRefundConfirmationEmail(savedRefund);
                System.out.println("Email sent successfully!");
            } catch (Exception e) {
                System.err.println("Failed to send refund confirmation email!");
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
            
            return savedRefund;
        } else if ("REJECT".equals(decision)) {
            refund.setRefundStatus("REJECTED");
            Refund savedRefund = refundRepository.save(refund);
            
            // processDate'in null olmadığından emin ol
            if (savedRefund.getProcessDate() == null) {
                savedRefund.setProcessDate(now);
                savedRefund = refundRepository.save(savedRefund);
            }

            Order order = refund.getOrder();
            order.setOrderStatus("REFUND_REJECTED");
            orderService.saveOrder(order);
            
            return savedRefund;
        } else {
            throw new IllegalArgumentException("Invalid decision. Must be either 'APPROVE' or 'REJECT'.");
        }
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

    @Transactional
    public Map<String, Object> setProductPrice(String productId, Double price, Boolean publishProduct) {
        try {
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                throw new RuntimeException("Product not found");
            }

            Product product = productOpt.get();
            product.setPrice(price);
            if (product.getCost() == null) product.setCost(price * 0.7);
            if (publishProduct) product.setPublished(true);
            productRepository.save(product);

            Map<String, Object> map = new HashMap<>();
            map.put("productId", productId);
            map.put("price", price);
            return map;
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new RuntimeException("Product was concurrently updated. Try again.");
        }
    }

    public List<Product> getUnpublishedProducts() {
        return productRepository.findByPublished(false);
    }
}
