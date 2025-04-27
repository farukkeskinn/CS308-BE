package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.models.Product;
import edu.sabanciuniv.projectbackend.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductManagementControllerTest {

    @Mock ProductService productService;
    @InjectMocks ProductManagementController controller;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void createProduct_savesAndReturnsProduct() {
        Product p = new Product();
        when(productService.saveProduct(p)).thenReturn(p);

        Product result = controller.createProduct(p);

        assertEquals(p, result);
    }

    @Test
    void deleteProduct_callsServiceDelete() {
        controller.deleteProduct("p1");
        verify(productService).deleteProduct("p1");
    }
}