package edu.sabanciuniv.projectbackend.dto;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ProductDetailsResponseTest {
    @Test
    void constructorAndGetters() {
        ReviewResponse rr = new ReviewResponse(5, "Harika", "approved");
        ProductDetailsResponse resp = new ProductDetailsResponse(
                "Laptop", "X1", "SN123", "desc", 10, 999.0, 2, "HP", "img.jpg", List.of(rr)
        );
        assertEquals("Laptop", resp.getName());
        assertEquals("X1", resp.getModel());
        assertEquals("SN123", resp.getSerialNumber());
        assertEquals(10, resp.getQuantity());
        assertEquals(999.0, resp.getPrice());
        assertEquals("HP", resp.getDistributor());
        assertEquals("img.jpg", resp.getImageUrl());
        assertEquals(1, resp.getReviews().size());
    }
}