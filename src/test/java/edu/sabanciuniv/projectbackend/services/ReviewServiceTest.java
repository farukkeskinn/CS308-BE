package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Review;
import edu.sabanciuniv.projectbackend.repositories.ReviewRepository;
import edu.sabanciuniv.projectbackend.repositories.CustomerRepository;
import edu.sabanciuniv.projectbackend.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock ReviewRepository reviewRepository;
    @Mock CustomerRepository customerRepository;
    @Mock ProductRepository productRepository;

    @InjectMocks ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPendingReviews_returnsPendingList() {
        Review r1 = new Review(); r1.setApprovalStatus("pending");
        when(reviewRepository.findByApprovalStatus("pending")).thenReturn(List.of(r1));

        List<Review> result = reviewService.getPendingReviews();

        assertEquals(1, result.size());
        assertEquals("pending", result.get(0).getApprovalStatus());
    }

    @Test
    void updateReviewStatus_changesStatus() {
        Review review = new Review();
        review.setApprovalStatus("pending");
        when(reviewRepository.findById("r1")).thenReturn(Optional.of(review));
        when(reviewRepository.save(any())).thenReturn(review);

        Review result = reviewService.updateReviewStatus("r1", "approved");

        assertEquals("approved", result.getApprovalStatus());
    }

    @Test
    void submitReview_customerNotFound_throwsException() {
        when(customerRepository.findById("c1")).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () ->
                reviewService.submitReview("c1", "p1", 5, "Nice!")
        );
        assertTrue(ex.getMessage().contains("Customer not found"));
    }

    @Test
    void getReviewsByCustomerId_returnsList() {
        Review r = new Review();
        when(reviewRepository.findByCustomer_CustomerId("c1")).thenReturn(List.of(r));

        List<Review> result = reviewService.getReviewsByCustomerId("c1");
        assertEquals(1, result.size());
        assertEquals(r, result.get(0));
    }
}