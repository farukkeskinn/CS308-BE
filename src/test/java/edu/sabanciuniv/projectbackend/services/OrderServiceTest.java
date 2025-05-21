package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.models.OrderItem;
import edu.sabanciuniv.projectbackend.models.Product;
import edu.sabanciuniv.projectbackend.models.ShoppingCart;
import edu.sabanciuniv.projectbackend.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock OrderRepository orderRepository;
    @InjectMocks OrderService orderService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void getOrderById_returnsOrder() {
        Order order = new Order();
        order.setOrderId("o1");
        when(orderRepository.findById("o1")).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById("o1");
        assertNotNull(result);
        assertEquals("o1", result.getOrderId());
    }

    @Test
    void getOrderById_notFound_returnsNull() {
        when(orderRepository.findById("notfound")).thenReturn(Optional.empty());

        Order result = orderService.getOrderById("notfound");
        assertNull(result);
    }

    @Test
    void getAllOrders_returnsList() {
        Order o1 = new Order(); o1.setOrderId("o1");
        Order o2 = new Order(); o2.setOrderId("o2");
        when(orderRepository.findAll()).thenReturn(List.of(o1, o2));

        List<Order> result = orderService.getAllOrders();
        assertEquals(2, result.size());
        assertEquals("o1", result.get(0).getOrderId());
    }

    @Test
    void getOrdersByCustomer_returnsList() {
        Order o1 = new Order(); o1.setOrderId("o1");
        when(orderRepository.findByCustomer_CustomerId("c1")).thenReturn(List.of(o1));

        List<Order> result = orderService.getOrdersByCustomer("c1");
        assertEquals(1, result.size());
        assertEquals("o1", result.get(0).getOrderId());
    }

    @Test
    void saveOrder_savesAndReturns() {
        Order o = new Order();
        when(orderRepository.save(o)).thenReturn(o);

        Order result = orderService.saveOrder(o);
        assertEquals(o, result);
    }

    @Test
    void deleteOrder_callsRepositoryDelete() {
        orderService.deleteOrder("o1");
        verify(orderRepository).deleteById("o1");
    }

    @Test
    void createOrderFromCart_createsOrderWithItems() {
        ShoppingCart cart = mock(ShoppingCart.class);
        Product product = new Product();
        product.setPrice(100.0);
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        edu.sabanciuniv.projectbackend.models.ShoppingCartItem cartItem = mock(edu.sabanciuniv.projectbackend.models.ShoppingCartItem.class);
        when(cartItem.getProduct()).thenReturn(product);
        when(cartItem.getQuantity()).thenReturn(2);
        when(cart.getShoppingCartItems()).thenReturn(List.of(cartItem));
        when(cart.getCustomer()).thenReturn(null);

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.createOrderFromCart(cart);

        assertNotNull(result.getOrderId());
        assertEquals("PROCESSING", result.getOrderStatus());
        assertEquals(200.0, result.getTotalPrice());
        assertEquals(1, result.getOrderItems().size());
        assertEquals(2, result.getOrderItems().get(0).getQuantity());
    }

    @Test
    void cancelOrder_processingStatus_cancelsOrder() {
        Order o = new Order();
        o.setOrderId("o1");
        o.setOrderStatus("PROCESSING");
        when(orderRepository.findById("o1")).thenReturn(Optional.of(o));
        when(orderRepository.save(any(Order.class))).thenReturn(o);

        boolean result = orderService.cancelOrder("o1");
        assertTrue(result);
        assertEquals("CANCELLED", o.getOrderStatus());
    }

    @Test
    void cancelOrder_nonProcessingStatus_returnsFalse() {
        Order o = new Order();
        o.setOrderId("o2");
        o.setOrderStatus("SHIPPED");
        when(orderRepository.findById("o2")).thenReturn(Optional.of(o));

        boolean result = orderService.cancelOrder("o2");
        assertFalse(result);
        assertEquals("SHIPPED", o.getOrderStatus());
    }

    @Test
    void updateOrderStatus_updatesStatus() {
        Order o = new Order();
        o.setOrderId("o3");
        o.setOrderStatus("PROCESSING");
        when(orderRepository.findById("o3")).thenReturn(Optional.of(o));
        when(orderRepository.save(any(Order.class))).thenReturn(o);

        Order result = orderService.updateOrderStatus("o3", "DELIVERED");
        assertEquals("DELIVERED", result.getOrderStatus());
    }

    @Test
    void updateOrderStatus_notFound_returnsNull() {
        when(orderRepository.findById("notfound")).thenReturn(Optional.empty());
        Order result = orderService.updateOrderStatus("notfound", "DELIVERED");
        assertNull(result);
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void createOrderFromCart_withEmptyCart_createsEmptyOrder() {
        ShoppingCart cart = mock(ShoppingCart.class);
        when(cart.getShoppingCartItems()).thenReturn(List.of());
        when(cart.getCustomer()).thenReturn(null);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.createOrderFromCart(cart);

        assertNotNull(result.getOrderId());
        assertEquals("PROCESSING", result.getOrderStatus());
        assertEquals(0.0, result.getTotalPrice());
        assertTrue(result.getOrderItems().isEmpty());
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void updateOrderStatus_withInvalidStatus_returnsNull() {
        Order o = new Order();
        o.setOrderId("o1");
        o.setOrderStatus("PROCESSING");
        when(orderRepository.findById("o1")).thenReturn(Optional.of(o));

        Order result = orderService.updateOrderStatus("o1", "INVALID_STATUS");
        assertNull(result);
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void getOrdersByCustomer_withNoOrders_returnsEmptyList() {
        when(orderRepository.findByCustomer_CustomerId("c1")).thenReturn(List.of());

        List<Order> result = orderService.getOrdersByCustomer("c1");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}