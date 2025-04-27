package edu.sabanciuniv.projectbackend.repositories;

import edu.sabanciuniv.projectbackend.models.Review;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewRepositoryTest {
    @Test
    void findByProduct_ProductId_returnsReviews() {
        ReviewRepository repo = mock(ReviewRepository.class);
        Review r = new Review();
        when(repo.findByProduct_ProductId("p1")).thenReturn(List.of(r));

        List<Review> result = repo.findByProduct_ProductId("p1");
        assertEquals(1, result.size());
        assertEquals(r, result.get(0));
    }
}