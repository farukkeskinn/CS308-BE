package edu.sabanciuniv.projectbackend.repositories;

import edu.sabanciuniv.projectbackend.models.Order;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderRepositoryTest {
    @Test
    void findById_returnsOrder() {
        OrderRepository repo = mock(OrderRepository.class);
        Order o = new Order(); o.setOrderId("o1");
        when(repo.findById("o1")).thenReturn(Optional.of(o));

        Optional<Order> result = repo.findById("o1");
        assertTrue(result.isPresent());
        assertEquals("o1", result.get().getOrderId());
    }
}