package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.dto.ReviewRequest;
import edu.sabanciuniv.projectbackend.dto.ReviewResponseDetail;
import edu.sabanciuniv.projectbackend.dto.ReviewSummaryDTO;
import edu.sabanciuniv.projectbackend.models.Review;
import edu.sabanciuniv.projectbackend.services.ReviewService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Entity -> DTO dönüşümü
    private ReviewResponseDetail toDto(Review r) {
        return new ReviewResponseDetail(
                r.getReviewId(),
                r.getCustomer().getCustomerId(),
                r.getProduct().getProductId(),
                r.getRating(),
                r.getComment(),
                r.getReviewDate(),
                r.getApprovalStatus()
        );
    }

    // Tüm yorumlar ya da pending olanlar (DTO farkı)
    @GetMapping
    public List<?> listReviews(
            @RequestParam(value = "status", required = false) String status
    ) {
        if ("pending".equalsIgnoreCase(status)) {
            return reviewService.getPendingReviews()
                    .stream()
                    .map(r -> new ReviewSummaryDTO(
                            r.getReviewId(),
                            r.getRating(),
                            r.getComment(),
                            r.getApprovalStatus(),
                            r.getReviewDate()
                    ))
                    .collect(Collectors.toList());
        }

        return reviewService.getAllReviews()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/by-product/{productId}")
    public List<ReviewResponseDetail> getByProduct(@PathVariable String productId) {
        return reviewService
                .getReviewsByProductId(productId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/by-customer/{customerId}")
    public List<ReviewResponseDetail> getByCustomer(@PathVariable String customerId) {
        return reviewService
                .getReviewsByCustomerId(customerId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    // Yorum ID’si ile detaylı bilgi
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDetail> getById(@PathVariable("id") String reviewId) {
        Review r = reviewService.getReview(reviewId);
        if (r == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDto(r));
    }

    // Yeni yorum gönder
    @PostMapping(
            value = "/{customerId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ReviewResponseDetail> submit(
            @PathVariable String customerId,
            @RequestBody ReviewRequest request
    ) {
        Review saved = reviewService.submitReview(
                customerId,
                request.getProductId(),
                request.getRating(),
                request.getComment()
        );
        return ResponseEntity.ok(toDto(saved));
    }

    @PatchMapping("/{reviewId}/approval")
    public Review updateReviewApprovalStatus(
            @PathVariable("reviewId") String reviewId,
            @RequestParam("status") String status) {
        return reviewService.updateReviewStatus(reviewId, status);
    }

}
