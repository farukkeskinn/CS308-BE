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

        // 1) ADMIN
        Admin admin = adminRepo.findByEmail(email);
        if (admin != null) {
            System.out.println("DEBUG: Found admin in DB -> " + admin.getEmail() + " / " + admin.getPassword());
            if (passwordEncoder.matches(password, admin.getPassword())) {
                return "ADMIN";
            }
        }

        // 2) CUSTOMER
        Customer customer = customerRepo.findByEmail(email);
        if (customer != null) {
            System.out.println("DEBUG: Found customer in DB -> " + customer.getEmail() + " / " + customer.getPassword());
            if (passwordEncoder.matches(password, customer.getPassword())) {
                return "CUSTOMER";
            }
        }

        // 3) PRODUCT_MANAGER
        ProductManager pm = pmRepo.findByEmail(email);
        if (pm != null) {
            System.out.println("DEBUG: Found product manager in DB -> " + pm.getEmail() + " / " + pm.getPassword());
            if (passwordEncoder.matches(password, pm.getPassword())) {
                return "PRODUCT_MANAGER";
            }
        }

        // 4) SALES_MANAGER
        SalesManager sm = smRepo.findByEmail(email);
        if (sm != null) {
            System.out.println("DEBUG: Found sales manager in DB -> " + sm.getEmail() + " / " + sm.getPassword());
            if (passwordEncoder.matches(password, sm.getPassword())) {
                return "SALES_MANAGER";
            }
        }

        // NONE
        return null;
    }
}

