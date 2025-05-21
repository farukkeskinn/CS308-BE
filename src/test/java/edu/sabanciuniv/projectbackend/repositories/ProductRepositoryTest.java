package edu.sabanciuniv.projectbackend.repositories;

import edu.sabanciuniv.projectbackend.models.Product;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductRepositoryTest {
    @Test
    void findById_returnsProduct() {
        ProductRepository repo = mock(ProductRepository.class);
        Product p = new Product();
        when(repo.findById("p1")).thenReturn(Optional.of(p));

        Optional<Product> result = repo.findById("p1");
        assertTrue(result.isPresent());
        assertEquals(p, result.get());
    }
}