package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.models.Product;
import edu.sabanciuniv.projectbackend.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product-managers/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductManagementController {

    private final ProductService productService;

    public ProductManagementController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public Product getProduct(@PathVariable("productId") String productId) {
        return productService.getProductById(productId);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PutMapping("/{productId}")
    public Product updateProduct(@PathVariable("productId") String productId,
                                 @RequestBody Product product) {
        // Ensure the product's ID in the payload matches the path variable
        product.setProductId(productId);
        return productService.saveProduct(product);
    }

    @PatchMapping("/{productId}/stock")
    public Product updateProductStock(@PathVariable("productId") String productId,
                                      @RequestParam Integer quantity) {
        return productService.updateProductStock(productId, quantity);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable("productId") String productId) {
        productService.deleteProduct(productId);
    }

    @GetMapping("/{id}/name")
    public ResponseEntity<Map<String,String>> getProductName(@PathVariable String id) {
        Product p = productService.getProductById(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Collections.singletonMap("name", p.getName()));
    }
}
