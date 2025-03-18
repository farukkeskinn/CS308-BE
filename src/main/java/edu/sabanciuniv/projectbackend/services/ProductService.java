package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Product;
import edu.sabanciuniv.projectbackend.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return productRepository.save(product);
    }

    public void deleteProduct(String productId) {
        productRepository.deleteById(productId);
    }

    public Page<Product> getProductsByCategory(Integer categoryId, Pageable pageable) {
        return productRepository.findByCategory(categoryId, pageable);
    }

}
