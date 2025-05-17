package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.dto.PasswordChangeRequest;
import edu.sabanciuniv.projectbackend.models.Customer;
import edu.sabanciuniv.projectbackend.models.ShoppingCart;
import edu.sabanciuniv.projectbackend.repositories.CustomerRepository;
import edu.sabanciuniv.projectbackend.repositories.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomer(String customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    @Transactional
    public Customer saveCustomer(Customer customer) {
        Customer saved = customerRepository.save(customer);

        if (shoppingCartRepository.findByCustomer(saved) == null) {
            ShoppingCart cart = new ShoppingCart();
            cart.setCartId(UUID.randomUUID().toString());
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
            return customer.getCustomerId();
        }
        throw new RuntimeException("Customer not found with email: " + email);
    }

    @Transactional
    public boolean changePassword(String customerId, PasswordChangeRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), customer.getPassword())) {
            return false;
        }

        // Update password
        customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
        customerRepository.save(customer);
        return true;
    }
}