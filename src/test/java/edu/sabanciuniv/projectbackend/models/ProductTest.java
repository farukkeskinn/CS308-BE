package edu.sabanciuniv.projectbackend.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    @Test
    void productFields_setAndGetCorrectly() {
        Product p = new Product();
        p.setProductId("p1");
        p.setName("Laptop");
        p.setPrice(999.99);
        p.setQuantity(5);

        assertEquals("p1", p.getProductId());
        assertEquals("Laptop", p.getName());
        assertEquals(999.99, p.getPrice());
        assertEquals(5, p.getQuantity());
    }

    @Test
    void setAndGetCategory() {
        Product p = new Product();
        Category c = new Category();
        p.setCategory(c);
        assertEquals(c, p.getCategory());
    }
}