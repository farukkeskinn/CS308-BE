package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.models.Product;
import edu.sabanciuniv.projectbackend.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductManagementControllerTest {

    @Mock ProductService productService;
    @InjectMocks ProductManagementController controller;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void createProduct_savesAndReturnsProduct() {
        Map<String, Object> productData = new HashMap<>();
        productData.put("name", "Test Product");
        productData.put("model", "Test Model");
        productData.put("description", "Test Description");
        productData.put("cost", 100.0);
        productData.put("serialNumber", "SN123");
        productData.put("quantity", 10);
        productData.put("warranty_status", 1);
        productData.put("distributor", "Test Distributor");
        productData.put("image_url", "test.jpg");

        Product expectedProduct = new Product();
        expectedProduct.setName("Test Product");
        expectedProduct.setModel("Test Model");
        expectedProduct.setDescription("Test Description");
        expectedProduct.setCost(100.0);
        expectedProduct.setSerialNumber("SN123");
        expectedProduct.setQuantity(10);
        expectedProduct.setWarranty_status(1);
        expectedProduct.setDistributor("Test Distributor");
        expectedProduct.setImage_url("test.jpg");

        when(productService.saveProduct(any(Product.class))).thenReturn(expectedProduct);

        Product result = controller.createProduct(productData);

        assertEquals(expectedProduct.getName(), result.getName());
        assertEquals(expectedProduct.getModel(), result.getModel());
        assertEquals(expectedProduct.getDescription(), result.getDescription());
        assertEquals(expectedProduct.getCost(), result.getCost());
        assertEquals(expectedProduct.getSerialNumber(), result.getSerialNumber());
        assertEquals(expectedProduct.getQuantity(), result.getQuantity());
        assertEquals(expectedProduct.getWarranty_status(), result.getWarranty_status());
        assertEquals(expectedProduct.getDistributor(), result.getDistributor());
        assertEquals(expectedProduct.getImage_url(), result.getImage_url());
    }

    @Test
    void deleteProduct_callsServiceDelete() {
        controller.deleteProduct("p1");
        verify(productService).deleteProduct("p1");
    }
}