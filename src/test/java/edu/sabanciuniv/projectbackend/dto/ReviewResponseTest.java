package edu.sabanciuniv.projectbackend.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReviewResponseTest {
    @Test
    void gettersWork() {
        ReviewResponse rr = new ReviewResponse(4, "İyi", "pending");
        assertEquals(4, rr.getRating());
        assertEquals("İyi", rr.getComment());
        assertEquals("pending", rr.getApprovalStatus());
    }
}