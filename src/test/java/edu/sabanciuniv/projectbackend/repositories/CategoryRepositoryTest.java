package edu.sabanciuniv.projectbackend.repositories;

import edu.sabanciuniv.projectbackend.models.Category;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryRepositoryTest {
    @Test
    void findById_returnsCategory() {
        CategoryRepository repo = mock(CategoryRepository.class);
        Category c = new Category(); c.setCategoryId(1);
        when(repo.findById(1)).thenReturn(Optional.of(c));

        Optional<Category> result = repo.findById(1);
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getCategoryId());
    }
}