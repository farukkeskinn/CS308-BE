package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.dto.ProductDetailsResponse;
import edu.sabanciuniv.projectbackend.dto.ProductPublishedResponse;
import edu.sabanciuniv.projectbackend.dto.ReviewResponse;
import edu.sabanciuniv.projectbackend.models.Category;
import edu.sabanciuniv.projectbackend.models.Product;
import edu.sabanciuniv.projectbackend.repositories.ProductRepository;
import edu.sabanciuniv.projectbackend.services.CategoryService;
import edu.sabanciuniv.projectbackend.services.ProductService;
import edu.sabanciuniv.projectbackend.services.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, ReviewService reviewService, ProductRepository productRepository, CategoryService categoryService) {
        this.productService = productService;
        this.reviewService = reviewService;
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<ProductPublishedResponse>> getAllProducts() {
        List<Product> allProducts = productService.getAllProducts();

        List<ProductPublishedResponse> response = allProducts.stream()
                .map(ProductPublishedResponse::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductPublishedResponse> getProduct(@PathVariable("id") String productId) {
        Product product = productService.getProductById(productId);
        if (product != null && Boolean.TRUE.equals(product.getPublished()) && product.getPrice() != null && product.getPrice() > 0) {
            return ResponseEntity.ok(new ProductPublishedResponse(product));
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
                .map(review -> new ReviewResponse(
                        review.getRating(),
                        review.getComment(),
                        review.getApprovalStatus()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ProductDetailsResponse(product, reviewResponses));
    }

    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<Page<ProductPublishedResponse>> getProductsByCategory(
            @PathVariable("categoryId") Integer categoryId,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<Product> products = productService.getProductsByCategory(categoryId, pageable);
        Page<ProductPublishedResponse> dtoPage = products
                .map(ProductPublishedResponse::new);

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}/recommended")
    public ResponseEntity<Page<ProductPublishedResponse>> getRecommendedProducts(
            @PathVariable("id") String productId,
            @PageableDefault(page = 0, size = 5) Pageable pageable) {

        Product product = productService.getProductById(productId);
        if (product == null || product.getCategory() == null) {
            return ResponseEntity.notFound().build();
        }

        Page<Product> recommendedProducts = productService.getRecommendedProducts(product.getCategory().getCategoryId(), productId, pageable);
        Page<ProductPublishedResponse> dtoPage = recommendedProducts
                .map(ProductPublishedResponse::new);

        return ResponseEntity.ok(dtoPage);
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
    public ResponseEntity<List<ProductPublishedResponse>> getPublishedProducts() {
        List<Product> publishedProducts = productRepository.findByPublishedTrueAndPriceGreaterThan(0.0);

        List<ProductPublishedResponse> response = publishedProducts.stream()
                .map(ProductPublishedResponse::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/category/{categoryId}")
    public ResponseEntity<ProductPublishedResponse> changeCategory(
            @PathVariable("id") String productId,
            @PathVariable("categoryId") Integer categoryId) {

        try {
            Product updated = productService.updateCategory(productId, categoryId);
            return ResponseEntity.ok(new ProductPublishedResponse(updated));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/category/name/{categoryName}")
    public ResponseEntity<ProductPublishedResponse> changeCategoryByName(
            @PathVariable("id")           String productId,
            @PathVariable("categoryName") String categoryName) {

        try {
            // load the Product
            Product product = productService.getProductById(productId);
            if (product == null) {
                return ResponseEntity.notFound().build();
            }

            // resolve Category by name (or 404)
            Category cat = categoryService.getByName(categoryName);

            // delegate into service
            Product updated = productService.updateCategory(product, cat);

            return ResponseEntity.ok(new ProductPublishedResponse(updated));

        } catch (NoSuchElementException ex) {
            // categoryService.getByName(...) or productService.updateCategory(...) threw
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

}
