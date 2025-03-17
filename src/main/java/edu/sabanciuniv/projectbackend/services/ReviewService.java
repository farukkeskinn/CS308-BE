package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Review;
import edu.sabanciuniv.projectbackend.repositories.ReviewRepository;
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
        return reviewRepository.save(review);
    }

    public void deleteReview(String reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
