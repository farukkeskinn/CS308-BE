package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Category;
import edu.sabanciuniv.projectbackend.models.Product;
import edu.sabanciuniv.projectbackend.repositories.CategoryRepository;
import edu.sabanciuniv.projectbackend.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    @Transactional
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Integer categoryId) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (!categoryOpt.isPresent()) {
            throw new RuntimeException("Category not found with ID: " + categoryId);
        }
        Category category = categoryOpt.get();
        Category parentCategory = category.getParentCategory();

        // Find products whose category exactly matches the deleted category
        List<Product> affectedProducts = productRepository.findByCategory_CategoryId(categoryId);

        if (parentCategory != null) {
            // For a subcategory, reassign its products to the parent category.
            for (Product product : affectedProducts) {
                product.setCategory(parentCategory);
            }
        } else {
            // For a main category, set the category of its products to null.
            for (Product product : affectedProducts) {
                product.setCategory(null);
            }
        }
        // Save the updates
        productRepository.saveAll(affectedProducts);
        // Finally, delete the category
        categoryRepository.delete(category);
    }

    public Category getByName(String name) {
        return categoryRepository.findByCategoryName(name)
                .orElseThrow(() -> new NoSuchElementException("Category not found: " + name));
    }
}
