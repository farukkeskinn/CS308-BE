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
        System.out.println("========== AUTH SERVICE START ==========");
        System.out.println("Checking login for: " + email);

        // Convert email to lowercase for consistent lookup
        email = email.toLowerCase().trim();

        // 1) ADMIN
        Admin admin = adminRepo.findByEmail(email);
        if (admin != null) {
            System.out.println("Found Admin: " + admin.getEmail());
            if (passwordEncoder.matches(password, admin.getPassword())) {
                System.out.println("Admin login successful!");
                System.out.println("=======================================");
                return "ADMIN";
            } else {
                System.out.println("Password mismatch!");
            }
        }

        // 2) CUSTOMER
        Customer customer = customerRepo.findByEmail(email);
        if (customer != null) {
            System.out.println("Found Customer: " + customer.getEmail());
            if (passwordEncoder.matches(password, customer.getPassword())) {
                System.out.println("Customer login successful!");
                System.out.println("=======================================");
                return "CUSTOMER";
            } else {
                System.out.println("Password mismatch!");
            }
        }

        // 3) PRODUCT_MANAGER
        ProductManager pm = pmRepo.findByEmail(email);
        if (pm != null) {
            System.out.println("Found Product Manager: " + pm.getEmail());
            if (passwordEncoder.matches(password, pm.getPassword())) {
                System.out.println("Product Manager login successful!");
                System.out.println("=======================================");
                return "PRODUCT_MANAGER";
            } else {
                System.out.println("Password mismatch!");
            }
        }

        // 4) SALES_MANAGER
        SalesManager sm = smRepo.findByEmail(email);
        if (sm != null) {
            System.out.println("Found Sales Manager: " + sm.getEmail());
            if (passwordEncoder.matches(password, sm.getPassword())) {
                System.out.println("Sales Manager login successful!");
                System.out.println("=======================================");
                return "SALES_MANAGER";
            } else {
                System.out.println("Password mismatch!");
            }
        }

        System.out.println("ERROR: No user found with this email!");
        System.out.println("=======================================");
        return null;
    }
}

