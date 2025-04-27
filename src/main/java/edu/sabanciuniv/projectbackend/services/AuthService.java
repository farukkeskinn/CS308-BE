package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.*;
import edu.sabanciuniv.projectbackend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private ProductManagerRepository pmRepo;

    @Autowired
    private SalesManagerRepository smRepo;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    public String register(String firstName, String lastName, String email, String password) {
        if (customerRepo.findByEmail(email) == null) {
            String encryptedPassword = passwordEncoder.encode(password);
            Customer newCustomer = new Customer(firstName, lastName, email, encryptedPassword);
            Customer saved = customerRepo.save(newCustomer);

            if (shoppingCartRepository.findByCustomer(saved) == null) {
                ShoppingCart cart = new ShoppingCart();
                cart.setCartId(UUID.randomUUID().toString());
                cart.setCartStatus("EMPTY");
                cart.setCustomer(saved);
                shoppingCartRepository.save(cart);
            }

            return "CUSTOMER";
        }
        return null;
    }

    public String login(String email, String password) {
        email = email.toLowerCase().trim();

        Customer customer = customerRepo.findByEmail(email);
        if (customer != null && passwordEncoder.matches(password, customer.getPassword())) {
            return "CUSTOMER";
        }

        ProductManager pm = pmRepo.findByEmail(email);
        if (pm != null && passwordEncoder.matches(password, pm.getPassword())) {
            return "PRODUCT_MANAGER";
        }

        SalesManager sm = smRepo.findByEmail(email);
        if (sm != null && passwordEncoder.matches(password, sm.getPassword())) {
            return "SALES_MANAGER";
        }

        return null;
    }
}