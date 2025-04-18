package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Product;
import edu.sabanciuniv.projectbackend.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(String productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public Product saveProduct(Product product) {
        if (product.getProductId() == null || product.getProductId().trim().isEmpty()) {
            product.setProductId(UUID.randomUUID().toString());
        }
        return productRepository.save(product);
    }

    public void deleteProduct(String productId) {
        productRepository.deleteById(productId);
    }

    public Page<Product> getProductsByCategory(Integer categoryId, Pageable pageable) {
        return productRepository.findByCategory(categoryId, pageable);
    }

    public Page<Product> getRecommendedProducts(Integer categoryId, String excludeProductId, Pageable pageable) {
        return productRepository.findRecommendedProducts(categoryId, excludeProductId, pageable);
    }

    public Product updateProductStock(String productId, Integer newStock) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setQuantity(newStock);
        return productRepository.save(product);
    }
}
