package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.models.ProductManager;
import edu.sabanciuniv.projectbackend.services.ProductManagerService;
import edu.sabanciuniv.projectbackend.services.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-managers")
public class ProductManagerController {

    private final ProductManagerService productManagerService;

    public ProductManagerController(ProductManagerService productManagerService, ProductService productService) {
        this.productManagerService = productManagerService;
    }

    @GetMapping
    public List<ProductManager> getAllProductManagers() {
        return productManagerService.getAllProductManagers();
    }

    @GetMapping("/{id}")
    public ProductManager getProductManagerById(@PathVariable("id") String pmId) {
        return productManagerService.getProductManager(pmId);
    }

    @PostMapping
    public ProductManager createProductManager(@RequestBody ProductManager productManager) {
        return productManagerService.saveProductManager(productManager);
    }

    @DeleteMapping("/{id}")
    public void deleteProductManager(@PathVariable("id") String pmId) {
        productManagerService.deleteProductManager(pmId);
    }

}


