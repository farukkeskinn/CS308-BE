package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.models.Product;
import edu.sabanciuniv.projectbackend.services.ProductService;
import edu.sabanciuniv.projectbackend.services.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock ProductService productService;
    @Mock ReviewService reviewService;

    @InjectMocks ProductController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProductDetails_returnsOk() {
        Product product = new Product();
        product.setProductId("p1");
        when(productService.getProductById("p1")).thenReturn(product);
        when(reviewService.getReviewsByProductId("p1")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = controller.getProductDetails("p1");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void getProductDetails_notFound_returns404() {
        when(productService.getProductById("p2")).thenReturn(null);

        ResponseEntity<?> response = controller.getProductDetails("p2");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void deleteProduct_callsServiceDelete() {
        controller.deleteProduct("p1");
        verify(productService).deleteProduct("p1");
    }

    @Test
    void getProductName_returnsName() {
        Product p = new Product();
        p.setName("Telefon");
        when(productService.getProductById("p1")).thenReturn(p);

        ResponseEntity<Map<String,String>> response = controller.getProductName("p1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Telefon", response.getBody().get("name"));
    }

    @Test
    void getProductName_notFound_returns404() {
        when(productService.getProductById("p2")).thenReturn(null);

        ResponseEntity<Map<String,String>> response = controller.getProductName("p2");

        assertEquals(404, response.getStatusCodeValue());
    }
}