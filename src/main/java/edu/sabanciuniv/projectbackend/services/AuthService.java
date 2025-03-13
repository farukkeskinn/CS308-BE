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

@Service
public class AuthService {

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private ProductManagerRepository pmRepo;

    @Autowired
    private SalesManagerRepository smRepo;

    public String login(String email, String password) {

        // 1) ADMIN
        Admin admin = adminRepo.findByEmail(email);
        if (admin != null) {
            System.out.println("DEBUG: Found admin in DB -> " + admin.getEmail() + " / " + admin.getPassword());
            if (admin.getPassword().equals(password)) {
                return "ADMIN";
            }
        }

        // 2) CUSTOMER
        Customer customer = customerRepo.findByEmail(email);
        if (customer != null) {
            System.out.println("DEBUG: Found customer in DB -> " + customer.getEmail() + " / " + customer.getPassword());
            if (customer.getPassword().equals(password)) {
                return "CUSTOMER";
            }
        }

        // 3) PRODUCT_MANAGER
        ProductManager pm = pmRepo.findByEmail(email);
        if (pm != null) {
            System.out.println("DEBUG: Found product manager in DB -> " + pm.getEmail() + " / " + pm.getPassword());
            if (pm.getPassword().equals(password)) {
                return "PRODUCT_MANAGER";
            }
        }

        // 4) SALES_MANAGER
        SalesManager sm = smRepo.findByEmail(email);
        if (sm != null) {
            System.out.println("DEBUG: Found sales manager in DB -> " + sm.getEmail() + " / " + sm.getPassword());
            if (sm.getPassword().equals(password)) {
                return "SALES_MANAGER";
            }
        }

        // NONE
        return null;
    }
}

