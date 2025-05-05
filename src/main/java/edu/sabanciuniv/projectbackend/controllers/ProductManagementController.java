package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.models.Category;
import edu.sabanciuniv.projectbackend.models.Product;
import edu.sabanciuniv.projectbackend.services.ProductService;
import org.springframework.http.MediaType;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Product createProduct(@RequestBody Map<String, Object> productData) {
        Product product = new Product();
        product.setName((String) productData.get("name"));
        product.setModel((String) productData.get("model"));
        product.setDescription((String) productData.get("description"));
        product.setCost(((Number) productData.get("cost")).doubleValue());
        product.setSerialNumber((String) productData.get("serialNumber"));
        product.setQuantity(((Number) productData.get("quantity")).intValue());
        product.setWarranty_status(((Number) productData.get("warranty_status")).intValue());
        product.setDistributor((String) productData.get("distributor"));
        product.setImage_url((String) productData.get("image_url"));

        if (productData.containsKey("categoryId")) {
            Category category = new Category();
            category.setCategoryId(((Number) productData.get("categoryId")).intValue());
            product.setCategory(category);
        }

        return productService.saveProduct(product);
    }

    @PutMapping("/{productId}")
    public Product updateProduct(@PathVariable("productId") String productId,
                                 @RequestBody Product product) {
        // Ensure the product's ID in the payload matches the path variable
        product.setProductId(productId);

        Product existingProduct = productService.getProductById(productId);
        if (existingProduct != null) {
            product.setPrice(existingProduct.getPrice());
            product.setPublished(existingProduct.getPublished());

            if (product.getCost() != existingProduct.getCost()) {
                product.setCost(existingProduct.getCost());
            }
        }

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
