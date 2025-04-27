package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.dto.ReviewRequest;
import edu.sabanciuniv.projectbackend.dto.ReviewResponseDetail;
import edu.sabanciuniv.projectbackend.dto.ReviewSummaryDTO;
import edu.sabanciuniv.projectbackend.models.Customer;
import edu.sabanciuniv.projectbackend.models.Product;
import edu.sabanciuniv.projectbackend.models.Review;
import edu.sabanciuniv.projectbackend.services.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewControllerTest {

    @Mock ReviewService reviewService;
    @InjectMocks ReviewController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listReviews_returnsAllReviewsAsDto() {
        Review review = new Review();
        Customer customer = new Customer();
        customer.setCustomerId("c1");
        Product product = new Product();
        product.setProductId("p1");
        review.setCustomer(customer);
        review.setProduct(product);
        review.setReviewId("r1");
        review.setRating(5);
        review.setComment("Harika");
        review.setReviewDate(LocalDateTime.now());
        review.setApprovalStatus("approved");

        when(reviewService.getAllReviews()).thenReturn(List.of(review));

        List<?> result = controller.listReviews(null);

        assertEquals(1, result.size());
        Object dto = result.get(0);
        assertTrue(dto instanceof ReviewResponseDetail);
        assertEquals("r1", ((ReviewResponseDetail) dto).getReviewId());
    }

    @Test
    void listReviews_withPendingStatus_returnsSummaryDto() {
        Review review = new Review();
        review.setReviewId("r2");
        review.setRating(4);
        review.setComment("İyi");
        review.setApprovalStatus("pending");
        review.setReviewDate(LocalDateTime.now());

        when(reviewService.getPendingReviews()).thenReturn(List.of(review));

        List<?> result = controller.listReviews("pending");

        assertEquals(1, result.size());
        Object dto = result.get(0);
        assertTrue(dto instanceof ReviewSummaryDTO);
        assertEquals("r2", ((ReviewSummaryDTO) dto).getReviewId());
    }

    @Test
    void getById_found_returnsDto() {
        Review review = new Review();
        Customer customer = new Customer();
        customer.setCustomerId("c1");
        Product product = new Product();
        product.setProductId("p1");
        review.setCustomer(customer);
        review.setProduct(product);
        review.setReviewId("r1");
        review.setRating(5);
        review.setComment("Harika");
        review.setReviewDate(LocalDateTime.now());
        review.setApprovalStatus("approved");

        when(reviewService.getReview("r1")).thenReturn(review);

        ResponseEntity<ReviewResponseDetail> response = controller.getById("r1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("r1", response.getBody().getReviewId());
    }

    @Test
    void getById_notFound_returns404() {
        when(reviewService.getReview("r404")).thenReturn(null);

        ResponseEntity<ReviewResponseDetail> response = controller.getById("r404");

        assertEquals(404, response.getStatusCodeValue());
    }
}