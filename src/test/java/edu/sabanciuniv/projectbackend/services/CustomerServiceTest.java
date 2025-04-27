package edu.sabanciuniv.projectbackend;

import edu.sabanciuniv.projectbackend.models.Customer;
import edu.sabanciuniv.projectbackend.models.ShoppingCart;
import edu.sabanciuniv.projectbackend.repositories.CustomerRepository;
import edu.sabanciuniv.projectbackend.repositories.ShoppingCartRepository;
import edu.sabanciuniv.projectbackend.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerServiceTest {

    private CustomerRepository customerRepository;
    private ShoppingCartRepository shoppingCartRepository;
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        customerRepository = mock(CustomerRepository.class);
        shoppingCartRepository = mock(ShoppingCartRepository.class);
        customerService = new CustomerService(customerRepository, shoppingCartRepository);
    }

    @Test
    public void testSaveCustomer_shouldCallCustomerRepositorySave() {
        // Arrange
        Customer customer = new Customer("Ali", "Veli", "ali@example.com", "12345");

        when(customerRepository.save(customer)).thenReturn(customer);
        when(shoppingCartRepository.findByCustomer(customer)).thenReturn(null);

        // Act
        Customer savedCustomer = customerService.saveCustomer(customer);

        // Assert
        verify(customerRepository, times(1)).save(customer);
        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
        assertEquals("Ali", savedCustomer.getFirstName());
    }

    @Test
    public void testGetAllCustomers_shouldReturnEmptyListWhenNoCustomersExist() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Customer> customers = customerService.getAllCustomers();

        // Assert
        assertNotNull(customers);
        assertTrue(customers.isEmpty());
        verify(customerRepository, times(1)).findAll();
    }


    @Test
    public void testDeleteCustomer_shouldCallCustomerRepositoryDeleteById() {
        // Arrange
        String customerId = "test-customer-id";

        // Act
        customerService.deleteCustomer(customerId);

        // Assert
        verify(customerRepository, times(1)).deleteById(customerId);
    }

    @Test
    public void testGetCustomerIdByEmail_shouldReturnCorrectId() {
        // Arrange
        Customer customer = new Customer("Ali", "Veli", "ali@example.com", "12345");
        customer.setCustomerId("mocked-customer-id");

        when(customerRepository.findByEmail("ali@example.com")).thenReturn(customer);

        // Act
        String customerId = customerService.getCustomerIdByEmail("ali@example.com");

        // Assert
        assertEquals("mocked-customer-id", customerId);
        verify(customerRepository, times(1)).findByEmail("ali@example.com");
    }

    @Test
    public void testGetCustomerIdByEmail_shouldThrowExceptionWhenCustomerNotFound() {
        // Arrange
        when(customerRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.getCustomerIdByEmail("nonexistent@example.com");
        });

        assertEquals("Customer not found with email: nonexistent@example.com", exception.getMessage());
        verify(customerRepository, times(1)).findByEmail("nonexistent@example.com");
    }



}
