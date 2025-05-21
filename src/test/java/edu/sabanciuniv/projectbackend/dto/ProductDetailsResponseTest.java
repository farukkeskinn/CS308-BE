package edu.sabanciuniv.projectbackend.dto;

import edu.sabanciuniv.projectbackend.models.Product;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ProductDetailsResponseTest {
    @Test
    void constructorAndGetters() {
        // Create a Product object
        Product product = new Product();
        product.setName("Laptop");
        product.setModel("X1");
        product.setSerialNumber("SN123");
        product.setDescription("desc");
        product.setQuantity(10);
        product.setPrice(999.0);
        product.setWarranty_status(2);
        product.setDistributor("HP");
        product.setImage_url("img.jpg");

        // Create a review
        ReviewResponse rr = new ReviewResponse(5, "Harika", "approved");
        
        // Create ProductDetailsResponse using the constructor
        ProductDetailsResponse resp = new ProductDetailsResponse(product, List.of(rr));

        // Assert the values
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
