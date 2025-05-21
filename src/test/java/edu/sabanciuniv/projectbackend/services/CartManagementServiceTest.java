package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.*;
import edu.sabanciuniv.projectbackend.dto.*;
import edu.sabanciuniv.projectbackend.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartManagementServiceTest {

    @Mock ShoppingCartRepository cartRepository;
    @Mock ShoppingCartItemRepository cartItemRepository;
    @Mock CustomerService customerService;
    @Mock ProductService productService;

    @InjectMocks CartManagementService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addItemToCart_guestUser_returnsDummyCart() {
        AddItemRequest request = mock(AddItemRequest.class);
        when(request.getCustomerId()).thenReturn(null);

        ShoppingCart result = cartService.addItemToCart(request);

        assertEquals("GUEST_FLOW", result.getCartId());
        verifyNoInteractions(cartRepository, cartItemRepository);
    }

    @Test
    void addItemToCart_invalidCustomer_returnsNull() {
        AddItemRequest request = mock(AddItemRequest.class);
        when(request.getCustomerId()).thenReturn("invalid");
        when(customerService.getCustomerById("invalid")).thenReturn(null);

        ShoppingCart result = cartService.addItemToCart(request);

        assertNull(result);
    }

    @Test
    void addItemToCart_insufficientStock_returnsNull() {
        AddItemRequest request = mock(AddItemRequest.class);
        when(request.getCustomerId()).thenReturn("c1");
        when(request.getProductId()).thenReturn("p1");
        when(request.getQuantity()).thenReturn(10);

        Customer customer = mock(Customer.class);
        Product product = mock(Product.class);
        when(product.getStock()).thenReturn(5);

        when(customerService.getCustomerById("c1")).thenReturn(customer);
        when(cartRepository.findByCustomer(customer)).thenReturn(null);
        when(productService.getProductById("p1")).thenReturn(product);

        ShoppingCart result = cartService.addItemToCart(request);

        assertNull(result);
    }

    @Test
    void calculateCartTotal_returnsCorrectSum() {
        ShoppingCart cart = mock(ShoppingCart.class);
        ShoppingCartItem item1 = mock(ShoppingCartItem.class);
        ShoppingCartItem item2 = mock(ShoppingCartItem.class);
        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);

        when(cartRepository.findById("cart1")).thenReturn(Optional.of(cart));
        when(cart.getShoppingCartItems()).thenReturn(List.of(item1, item2));
        when(item1.getProduct()).thenReturn(product1);
        when(item2.getProduct()).thenReturn(product2);
        when(product1.getPrice()).thenReturn(10.0);
        when(product2.getPrice()).thenReturn(20.0);
        when(item1.getQuantity()).thenReturn(2);
        when(item2.getQuantity()).thenReturn(1);

        double total = cartService.calculateCartTotal("cart1");

        assertEquals(40.0, total);
    }

    @Test
    void removePartialQuantity_quantityZero_itemDeleted() {
        ShoppingCartItem item = mock(ShoppingCartItem.class);
        ShoppingCart cart = mock(ShoppingCart.class);

        when(item.getQuantity()).thenReturn(2);
        when(cartItemRepository.findById("item1")).thenReturn(Optional.of(item));
        when(item.getShoppingCart()).thenReturn(cart);
        when(cart.getCartId()).thenReturn("cart1");
        when(cartRepository.findById("cart1")).thenReturn(Optional.of(cart));
        when(cartItemRepository.findAll()).thenReturn(List.of());

        cartService.removePartialQuantity("item1", 2);

        verify(cartItemRepository).deleteById("item1");
        verify(cart).setCartStatus("EMPTY");
        verify(cartRepository).save(cart);
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void calculateCartTotal_withEmptyCart_returnsZero() {
        ShoppingCart cart = mock(ShoppingCart.class);
        when(cartRepository.findById("cart1")).thenReturn(Optional.of(cart));
        when(cart.getShoppingCartItems()).thenReturn(List.of());

        double total = cartService.calculateCartTotal("cart1");
        assertEquals(0.0, total);
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void calculateCartTotal_withNullCart_returnsZero() {
        when(cartRepository.findById("cart1")).thenReturn(Optional.empty());

        double total = cartService.calculateCartTotal("cart1");
        assertEquals(0.0, total);
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void addItemToCart_withExistingCart_addsToExistingCart() {
        AddItemRequest request = mock(AddItemRequest.class);
        when(request.getCustomerId()).thenReturn("c1");
        when(request.getProductId()).thenReturn("p1");
        when(request.getQuantity()).thenReturn(1);

        Customer customer = mock(Customer.class);
        Product product = mock(Product.class);
        ShoppingCart existingCart = mock(ShoppingCart.class);
        List<ShoppingCartItem> items = new ArrayList<>();

        when(customerService.getCustomerById("c1")).thenReturn(customer);
        when(cartRepository.findByCustomer(customer)).thenReturn(existingCart);
        when(productService.getProductById("p1")).thenReturn(product);
        when(product.getStock()).thenReturn(10);
        when(existingCart.getShoppingCartItems()).thenReturn(items);
        when(cartItemRepository.save(any(ShoppingCartItem.class))).thenAnswer(i -> i.getArgument(0));

        ShoppingCart result = cartService.addItemToCart(request);

        assertEquals(existingCart, result);
        verify(cartItemRepository).save(any(ShoppingCartItem.class));
    }
}