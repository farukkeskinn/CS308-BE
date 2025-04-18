package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Customer;
import edu.sabanciuniv.projectbackend.models.ShoppingCart;
import edu.sabanciuniv.projectbackend.repositories.CustomerRepository;
import edu.sabanciuniv.projectbackend.repositories.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    public CustomerService(CustomerRepository customerRepository,
                           ShoppingCartRepository shoppingCartRepository) {
        this.customerRepository = customerRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomer(String customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    public Customer saveCustomer(Customer customer) {

        Customer saved = customerRepository.save(customer);

        ShoppingCart existing = shoppingCartRepository.findByCustomer(saved);
        if (existing == null) {
            ShoppingCart cart = new ShoppingCart();
            cart.setCartId(saved.getCustomerId());
            cart.setCartStatus("EMPTY");
            cart.setCustomer(saved);
            shoppingCartRepository.save(cart);
        }

        return saved;
    }

    public void deleteCustomer(String customerId) {
        customerRepository.deleteById(customerId);
    }

    public Customer getCustomerById(String customerId) {
        return getCustomer(customerId);
    }


    public String getCustomerIdByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer != null) {
            return customer.getCustomerId(); // Ensure this returns a String
        }
        throw new RuntimeException("Customer not found with email: " + email);
    }
}