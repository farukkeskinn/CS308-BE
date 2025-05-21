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

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void saveOrderItem_withExistingItem_updatesItem() {
        OrderItem item = new OrderItem();
        item.setOrderItemId("existing");
        when(orderItemRepository.save(item)).thenReturn(item);

        OrderItem result = orderItemService.saveOrderItem(item);
        assertEquals("existing", result.getOrderItemId());
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void getOrderItemById_withInvalidId_returnsNull() {
        when(orderItemRepository.findById("invalid")).thenReturn(Optional.empty());

        OrderItem result = orderItemService.getOrderItemById("invalid");
        assertNull(result);
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void deleteOrderItem_deletesItem() {
        orderItemService.deleteOrderItem("oi1");
        verify(orderItemRepository).deleteById("oi1");
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void saveOrderItem_savesAndReturns() {
        OrderItem item = new OrderItem();
        when(orderItemRepository.save(item)).thenReturn(item);

        OrderItem result = orderItemService.saveOrderItem(item);
        assertEquals(item, result);
    }
}