package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.models.Category;
import edu.sabanciuniv.projectbackend.services.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-managers/categories")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryManagementController {

    private final CategoryService categoryService;

    public CategoryManagementController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // GET all categories
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // GET a specific category by ID
    @GetMapping("/{categoryId}")
    public Category getCategory(@PathVariable("categoryId") Integer categoryId) {
        return categoryService.getCategory(categoryId);
    }

    // POST to add a new category
    @PostMapping
    public Category addCategory(@RequestBody Category category) {
        return categoryService.saveCategory(category);
    }

    // PUT to update an existing category
    @PutMapping("/{categoryId}")
    public Category updateCategory(@PathVariable("categoryId") Integer categoryId,
                                   @RequestBody Category category) {
        // Ensure the category's id is set correctly
        category.setCategoryId(categoryId);
        return categoryService.saveCategory(category);
    }

    // DELETE to remove a category
    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable("categoryId") Integer categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}
