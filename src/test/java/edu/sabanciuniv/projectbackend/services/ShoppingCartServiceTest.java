package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.ShoppingCart;
import edu.sabanciuniv.projectbackend.models.ShoppingCartItem;
import edu.sabanciuniv.projectbackend.repositories.ShoppingCartRepository;
import edu.sabanciuniv.projectbackend.repositories.ShoppingCartItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingCartServiceTest {

    @Mock ShoppingCartRepository shoppingCartRepository;
    @Mock ShoppingCartItemRepository shoppingCartItemRepository;
    @InjectMocks ShoppingCartService shoppingCartService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void getShoppingCart_returnsCart() {
        ShoppingCart cart = new ShoppingCart();
        cart.setCartId("cart1");
        when(shoppingCartRepository.findById("cart1")).thenReturn(Optional.of(cart));

        ShoppingCart result = shoppingCartService.getShoppingCart("cart1");
        assertNotNull(result);
        assertEquals("cart1", result.getCartId());
    }

    @Test
    void getShoppingCart_notFound_returnsNull() {
        when(shoppingCartRepository.findById("notfound")).thenReturn(Optional.empty());

        ShoppingCart result = shoppingCartService.getShoppingCart("notfound");
        assertNull(result);
    }

    @Test
    void getAllShoppingCarts_returnsList() {
        ShoppingCart c1 = new ShoppingCart(); c1.setCartId("c1");
        ShoppingCart c2 = new ShoppingCart(); c2.setCartId("c2");
        when(shoppingCartRepository.findAll()).thenReturn(List.of(c1, c2));

        List<ShoppingCart> result = shoppingCartService.getAllShoppingCarts();
        assertEquals(2, result.size());
        assertEquals("c1", result.get(0).getCartId());
    }

    @Test
    void saveShoppingCart_savesAndReturns() {
        ShoppingCart cart = new ShoppingCart();
        when(shoppingCartRepository.save(cart)).thenReturn(cart);

        ShoppingCart result = shoppingCartService.saveShoppingCart(cart);
        assertEquals(cart, result);
    }

    @Test
    void deleteShoppingCart_callsRepositoryDelete() {
        shoppingCartService.deleteShoppingCart("cart1");
        verify(shoppingCartRepository).deleteById("cart1");
    }

    @Test
    void getCartByUsername_returnsCart() {
        ShoppingCart cart = new ShoppingCart();
        when(shoppingCartRepository.findByCustomerEmail("ali@su.edu")).thenReturn(cart);

        ShoppingCart result = shoppingCartService.getCartByUsername("ali@su.edu");
        assertEquals(cart, result);
    }

    @Test
    void clearCart_noCartOrEmpty_doesNothing() {
        when(shoppingCartRepository.findByCustomerEmail("notfound@su.edu")).thenReturn(null);

        shoppingCartService.clearCart("notfound@su.edu");

        verify(shoppingCartItemRepository, never()).deleteAll(anyList());
        verify(shoppingCartRepository, never()).save(any());
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void getShoppingCart_withItems_returnsCartWithItems() {
        ShoppingCart cart = new ShoppingCart();
        cart.setCartId("cart1");
        ShoppingCartItem item = new ShoppingCartItem();
        item.setQuantity(2);
        cart.getShoppingCartItems().add(item);
        when(shoppingCartRepository.findById("cart1")).thenReturn(Optional.of(cart));

        ShoppingCart result = shoppingCartService.getShoppingCart("cart1");
        assertNotNull(result);
        assertEquals(1, result.getShoppingCartItems().size());
        assertEquals(2, result.getShoppingCartItems().get(0).getQuantity());
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void getAllShoppingCarts_emptyList_returnsEmptyList() {
        when(shoppingCartRepository.findAll()).thenReturn(List.of());

        List<ShoppingCart> result = shoppingCartService.getAllShoppingCarts();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void clearCart_withValidCart_clearsItems() {
        ShoppingCart cart = new ShoppingCart();
        cart.setCartId("cart1");
        ShoppingCartItem item = new ShoppingCartItem();
        cart.getShoppingCartItems().add(item);
        when(shoppingCartRepository.findByCustomerEmail("test@su.edu")).thenReturn(cart);

        shoppingCartService.clearCart("test@su.edu");

        verify(shoppingCartItemRepository).deleteAll(cart.getShoppingCartItems());
        verify(shoppingCartRepository).save(cart);
        assertTrue(cart.getShoppingCartItems().isEmpty());
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void getShoppingCart_withInvalidId_returnsNull() {
        when(shoppingCartRepository.findById("invalid")).thenReturn(Optional.empty());

        ShoppingCart result = shoppingCartService.getShoppingCart("invalid");
        assertNull(result);
    }
}