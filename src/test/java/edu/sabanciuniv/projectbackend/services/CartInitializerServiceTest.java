package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Customer;
import edu.sabanciuniv.projectbackend.models.ShoppingCart;
import edu.sabanciuniv.projectbackend.repositories.CustomerRepository;
import edu.sabanciuniv.projectbackend.repositories.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.mockito.Mockito.*;

class CartInitializerServiceTest {

    @Mock CustomerRepository customerRepository;
    @Mock ShoppingCartRepository shoppingCartRepository;
    @InjectMocks CartInitializerService cartInitializerService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void initShoppingCartsForExistingCustomers_createsCartsForCustomersWithoutCart() {
        Customer c1 = new Customer(); c1.setCustomerId("c1");
        Customer c2 = new Customer(); c2.setCustomerId("c2");
        when(customerRepository.findAll()).thenReturn(List.of(c1, c2));
        when(shoppingCartRepository.findByCustomer(c1)).thenReturn(null);
        when(shoppingCartRepository.findByCustomer(c2)).thenReturn(new ShoppingCart());

        cartInitializerService.initShoppingCartsForExistingCustomers();

        verify(shoppingCartRepository, times(1)).save(argThat(cart -> cart.getCustomer() == c1));
        verify(shoppingCartRepository, never()).save(argThat(cart -> cart.getCustomer() == c2));
    }

    @Test
    void initShoppingCartsForExistingCustomers_noCustomers_noAction() {
        when(customerRepository.findAll()).thenReturn(List.of());

        cartInitializerService.initShoppingCartsForExistingCustomers();

        verifyNoInteractions(shoppingCartRepository);
    }

    @Test
    void initShoppingCartsForExistingCustomers_allHaveCarts_noNewCartCreated() {
        Customer c1 = new Customer(); c1.setCustomerId("c1");
        Customer c2 = new Customer(); c2.setCustomerId("c2");
        when(customerRepository.findAll()).thenReturn(List.of(c1, c2));
        when(shoppingCartRepository.findByCustomer(any())).thenReturn(new ShoppingCart());

        cartInitializerService.initShoppingCartsForExistingCustomers();

        verify(shoppingCartRepository, never()).save(any());
    }
}