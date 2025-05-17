package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Customer;
import edu.sabanciuniv.projectbackend.models.Product;
import edu.sabanciuniv.projectbackend.models.Review;
import edu.sabanciuniv.projectbackend.repositories.CustomerRepository;
import edu.sabanciuniv.projectbackend.repositories.ProductRepository;
import edu.sabanciuniv.projectbackend.repositories.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.models.OrderItem;
import edu.sabanciuniv.projectbackend.services.OrderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    public ReviewService(ReviewRepository reviewRepository,
                         CustomerRepository customerRepository,
                         ProductRepository productRepository,
                         OrderService orderService
    ) {
        this.reviewRepository = reviewRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderService = orderService;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReview(String reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    public List<Review> getReviewsByProductId(String productId) {
        return reviewRepository.findByProduct_ProductId(productId);
    }

    public List<Review> getReviewsByCustomerId(String customerId) {
        return reviewRepository.findByCustomer_CustomerId(customerId);
    }

    public List<Review> getPendingReviews() {
        return reviewRepository.findByApprovalStatus("pending");
    }

    @Transactional
    public Review updateReviewStatus(String reviewId, String newStatus) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found: " + reviewId));

        review.setApprovalStatus(newStatus);

        // Bu save çağrısı @Version varsa optimistic locking yapar
        return reviewRepository.save(review);
    }


    @Transactional
    public Review submitReview(String customerId,
                               String productId,
                               int rating,
                               String comment) {

        List<Order> orders = orderService.getOrdersByCustomer(customerId);
        boolean canReview = orders.stream()
                .filter(order -> "DELIVERED".equalsIgnoreCase(order.getOrderStatus()) ||
                        "REFUNDED".equalsIgnoreCase(order.getOrderStatus()))
                .flatMap(order -> order.getOrderItems().stream())
                .anyMatch(item -> item.getProduct().getProductId().equals(productId));

        if (!canReview) {
            throw new RuntimeException("Review can be done on only delivered or refunded orders.");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        Review review = new Review();
        review.setReviewId(UUID.randomUUID().toString());
        review.setCustomer(customer);
        review.setProduct(product);
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewDate(LocalDateTime.now());
        review.setApprovalStatus("pending");

        return reviewRepository.save(review);
    }
}