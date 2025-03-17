package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.dto.ProductDetailsResponse;
import edu.sabanciuniv.projectbackend.dto.ReviewResponse;
import edu.sabanciuniv.projectbackend.models.Product;
import edu.sabanciuniv.projectbackend.models.Review;
import edu.sabanciuniv.projectbackend.services.ProductService;
import edu.sabanciuniv.projectbackend.services.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;

    public ProductController(ProductService productService, ReviewService reviewService) {
        this.productService = productService;
        this.reviewService = reviewService;
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
                .map(review -> new ReviewResponse(review.getRating(), review.getComment()))
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

    @GetMapping("/by-main-category/{categoryId}")
    public List<Product> getProductsByMainCategory(@PathVariable("categoryId") Integer categoryId) {
        return productService.getProductsByMainCategory(categoryId);
    }

    @GetMapping("/by-sub-category/{categoryId}")
    public List<Product> getProductsBySubCategory(@PathVariable("categoryId") Integer categoryId) {
        return productService.getProductsBySubCategory(categoryId);
    }

}
