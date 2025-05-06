package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.dto.ProductDetailsResponse;
import edu.sabanciuniv.projectbackend.dto.ReviewResponse;
import edu.sabanciuniv.projectbackend.models.Product;
import edu.sabanciuniv.projectbackend.repositories.ProductRepository;
import edu.sabanciuniv.projectbackend.services.ProductService;
import edu.sabanciuniv.projectbackend.services.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;
    private final ProductRepository productRepository;

    public ProductController(ProductService productService, ReviewService reviewService, ProductRepository productRepository) {
        this.productService = productService;
        this.reviewService = reviewService;
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String productId) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") String productId) {
        productService.deleteProduct(productId);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<?> getProductDetails(@PathVariable("id") String productId) {
        Product product = productService.getProductById(productId);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        List<ReviewResponse> reviewResponses = reviewService.getReviewsByProductId(productId)
                .stream()
                .map(review -> new ReviewResponse(review.getRating(), review.getComment(), review.getApprovalStatus()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ProductDetailsResponse(
                product.getName(),
                product.getModel(),
                product.getSerialNumber(),
                product.getDescription(),
                product.getQuantity(),
                product.getPrice(),
                product.getWarranty_status(),
                product.getDistributor(),
                product.getImage_url(),
                reviewResponses
        ));
    }

    @GetMapping("/by-category/{categoryId}")
    public Page<Product> getProductsByCategory(
            @PathVariable("categoryId") Integer categoryId,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        return productService.getProductsByCategory(categoryId, pageable);
    }

    @GetMapping("/{id}/recommended")
    public ResponseEntity<Page<Product>> getRecommendedProducts(
            @PathVariable("id") String productId,
            @PageableDefault(page = 0, size = 5) Pageable pageable) {

        Product product = productService.getProductById(productId);
        if (product == null || product.getCategory() == null) {
            return ResponseEntity.notFound().build();
        }

        Page<Product> recommendedProducts = productService.getRecommendedProducts(product.getCategory().getCategoryId(), productId, pageable);
        return ResponseEntity.ok(recommendedProducts);
    }

    @GetMapping("/{id}/name")
    public ResponseEntity<Map<String,String>> getProductName(@PathVariable String id) {
        Product p = productService.getProductById(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Collections.singletonMap("name", p.getName()));
    }

    @GetMapping("/published")
    public List<Product> getPublishedProducts() {
        return productRepository.findByPublishedTrueAndPriceGreaterThan(0.0);
    }
}
