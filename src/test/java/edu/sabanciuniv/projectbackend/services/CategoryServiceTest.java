package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Category;
import edu.sabanciuniv.projectbackend.models.Product;
import edu.sabanciuniv.projectbackend.repositories.CategoryRepository;
import edu.sabanciuniv.projectbackend.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock CategoryRepository categoryRepository;
    @Mock ProductRepository productRepository;
    @InjectMocks CategoryService categoryService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void getCategory_returnsCategory() {
        Category c = new Category();
        c.setCategoryId(1);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(c));

        Category result = categoryService.getCategory(1);
        assertNotNull(result);
        assertEquals(1, result.getCategoryId());
    }

    @Test
    void getCategory_notFound_returnsNull() {
        when(categoryRepository.findById(2)).thenReturn(Optional.empty());

        Category result = categoryService.getCategory(2);
        assertNull(result);
    }

    @Test
    void getAllCategories_returnsList() {
        Category c1 = new Category(); c1.setCategoryId(1);
        Category c2 = new Category(); c2.setCategoryId(2);
        when(categoryRepository.findAll()).thenReturn(List.of(c1, c2));

        List<Category> result = categoryService.getAllCategories();
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getCategoryId());
    }

    @Test
    void saveCategory_savesAndReturns() {
        Category c = new Category();
        when(categoryRepository.save(c)).thenReturn(c);

        Category result = categoryService.saveCategory(c);
        assertEquals(c, result);
    }

    @Test
    void deleteCategory_withParentCategory_reassignsProducts() {
        Category sub = new Category(); sub.setCategoryId(2);
        Category parent = new Category(); parent.setCategoryId(1);
        sub.setParentCategory(parent);

        Product p1 = new Product(); p1.setCategory(sub);
        when(categoryRepository.findById(2)).thenReturn(Optional.of(sub));
        when(productRepository.findByCategory_CategoryId(2)).thenReturn(List.of(p1));

        categoryService.deleteCategory(2);

        verify(productRepository).saveAll(anyList());
        verify(categoryRepository).delete(sub);
        assertEquals(parent, p1.getCategory());
    }

    @Test
    void deleteCategory_withNoParentCategory_setsProductCategoryNull() {
        Category main = new Category(); main.setCategoryId(3);
        main.setParentCategory(null);

        Product p1 = new Product(); p1.setCategory(main);
        when(categoryRepository.findById(3)).thenReturn(Optional.of(main));
        when(productRepository.findByCategory_CategoryId(3)).thenReturn(List.of(p1));

        categoryService.deleteCategory(3);

        verify(productRepository).saveAll(anyList());
        verify(categoryRepository).delete(main);
        assertNull(p1.getCategory());
    }

    @Test
    void deleteCategory_notFound_throwsException() {
        when(categoryRepository.findById(99)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> categoryService.deleteCategory(99));
        assertTrue(ex.getMessage().contains("Category not found"));
    }
}