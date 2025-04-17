package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Review;
import edu.sabanciuniv.projectbackend.repositories.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
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

    public Review saveReview(Review review) {
        // Force the approvalStatus to "pending", regardless of what is sent from the frontend
        review.setApprovalStatus("pending");
        return reviewRepository.save(review);
    }

    public void deleteReview(String reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public List<Review> getPendingReviews() {
        return reviewRepository.findByApprovalStatus("pending");
    }

    @Transactional
    public Review updateReviewStatus(String reviewId, String newStatus) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with ID: " + reviewId));
        review.setApprovalStatus(newStatus);
        return reviewRepository.save(review);
    }

}
