package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Admin;
import edu.sabanciuniv.projectbackend.models.Customer;
import edu.sabanciuniv.projectbackend.models.ProductManager;
import edu.sabanciuniv.projectbackend.models.SalesManager;
import edu.sabanciuniv.projectbackend.repositories.AdminRepository;
import edu.sabanciuniv.projectbackend.repositories.CustomerRepository;
import edu.sabanciuniv.projectbackend.repositories.ProductManagerRepository;
import edu.sabanciuniv.projectbackend.repositories.SalesManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    public String register(String firstName, String lastName, String email, String password) {
        if (customerRepo.findByEmail(email) == null) {
            String encryptedPassword = passwordEncoder.encode(password);
            Customer newCustomer = new Customer(firstName, lastName, email, encryptedPassword);
            customerRepo.save(newCustomer);
            return "CUSTOMER";
        }
        return null;
    }

    public String login(String email, String password) {
        // Convert email to lowercase for consistent lookup
        email = email.toLowerCase().trim();

        // 1) CUSTOMER
        Customer customer = customerRepo.findByEmail(email);
        if (customer != null && passwordEncoder.matches(password, customer.getPassword())) {
            return "CUSTOMER";
        }

        // 2) PRODUCT_MANAGER
        ProductManager pm = pmRepo.findByEmail(email);
        if (pm != null && passwordEncoder.matches(password, pm.getPassword())) {
            return "PRODUCT_MANAGER";
        }

        // 3) SALES_MANAGER
        SalesManager sm = smRepo.findByEmail(email);
        if (sm != null && passwordEncoder.matches(password, sm.getPassword())) {
            return "SALES_MANAGER";
        }

        return null;
    }
}

