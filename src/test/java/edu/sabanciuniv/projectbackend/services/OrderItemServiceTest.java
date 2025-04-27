package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.OrderItem;
import edu.sabanciuniv.projectbackend.repositories.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderItemServiceTest {

    @Mock OrderItemRepository orderItemRepository;
    @InjectMocks OrderItemService orderItemService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void getOrderItemById_returnsItem() {
        OrderItem item = new OrderItem();
        item.setOrderItemId("oi1");
        when(orderItemRepository.findById("oi1")).thenReturn(Optional.of(item));

        OrderItem result = orderItemService.getOrderItemById("oi1");
        assertEquals("oi1", result.getOrderItemId());
    }

    @Test
    void getOrderItemById_notFound_returnsNull() {
        when(orderItemRepository.findById("notfound")).thenReturn(Optional.empty());

        OrderItem result = orderItemService.getOrderItemById("notfound");
        assertNull(result);
    }
}