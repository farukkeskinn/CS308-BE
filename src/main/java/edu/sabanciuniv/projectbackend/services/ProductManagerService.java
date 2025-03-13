package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.ProductManager;
import edu.sabanciuniv.projectbackend.repositories.ProductManagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductManagerService {

    private final ProductManagerRepository productManagerRepository;

    public ProductManagerService(ProductManagerRepository productManagerRepository) {
        this.productManagerRepository = productManagerRepository;
    }

    public List<ProductManager> getAllProductManagers() {
        return productManagerRepository.findAll();
    }

    public ProductManager getProductManager(String pmId) {
        return productManagerRepository.findById(pmId).orElse(null);
    }

    public ProductManager saveProductManager(ProductManager productManager) {
        return productManagerRepository.save(productManager);
    }

    public void deleteProductManager(String pmId) {
        productManagerRepository.deleteById(pmId);
    }
}
