package edu.sabanciuniv.projectbackend.models;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {
    @Test
    void reviewFields_setAndGetCorrectly() {
        Review r = new Review();
        r.setReviewId("r1");
        r.setRating(5);
        r.setComment("Çok iyi");
        r.setReviewDate(LocalDateTime.now());
        r.setApprovalStatus("pending");

        assertEquals("r1", r.getReviewId());
        assertEquals(5, r.getRating());
        assertEquals("Çok iyi", r.getComment());
        assertEquals("pending", r.getApprovalStatus());
    }
}