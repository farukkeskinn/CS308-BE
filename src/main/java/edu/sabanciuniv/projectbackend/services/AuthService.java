package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Admin;
import edu.sabanciuniv.projectbackend.models.Customer;
import edu.sabanciuniv.projectbackend.models.ProductManager;
import edu.sabanciuniv.projectbackend.models.SalesManager;
import edu.sabanciuniv.projectbackend.repositories.AdminRepository;
import edu.sabanciuniv.projectbackend.repositories.CustomerRepository;
import edu.sabanciuniv.projectbackend.repositories.ProductManagerRepository;
import edu.sabanciuniv.projectbackend.repositories.SalesManagerRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AdminRepository adminRepo;
    private final CustomerRepository customerRepo;
    private final ProductManagerRepository pmRepo;
    private final SalesManagerRepository smRepo;

    public AuthService(AdminRepository adminRepo, CustomerRepository customerRepo,
                       ProductManagerRepository pmRepo, SalesManagerRepository smRepo) {
        this.adminRepo = adminRepo;
        this.customerRepo = customerRepo;
        this.pmRepo = pmRepo;
        this.smRepo = smRepo;
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
            if (admin.getPassword().trim().equals(password.trim())) {
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
            if (customer.getPassword().trim().equals(password.trim())) {
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
            if (pm.getPassword().trim().equals(password.trim())) {
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
            if (sm.getPassword().trim().equals(password.trim())) {
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