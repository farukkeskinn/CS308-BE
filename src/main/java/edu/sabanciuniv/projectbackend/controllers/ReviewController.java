package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.models.Review;
import edu.sabanciuniv.projectbackend.services.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable("id") String reviewId) {
        return reviewService.getReview(reviewId);
    }

    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return reviewService.saveReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable("id") String reviewId) {
        reviewService.deleteReview(reviewId);
    }

    @PatchMapping("/{reviewId}/approval")
    public Review updateReviewApprovalStatus(
            @PathVariable("reviewId") String reviewId,
            @RequestParam("status") String status) {
        return reviewService.updateReviewStatus(reviewId, status);
    }

    @GetMapping
    public List<Review> getReviews(@RequestParam(value="status", required=false) String status) {
        if ("pending".equalsIgnoreCase(status)) {
            return reviewService.getPendingReviews();
        }
        return reviewService.getAllReviews();
    }
}
