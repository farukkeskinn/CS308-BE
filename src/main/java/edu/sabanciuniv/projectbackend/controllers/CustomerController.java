package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.dto.PasswordChangeRequest;
import edu.sabanciuniv.projectbackend.models.Customer;
import edu.sabanciuniv.projectbackend.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable("id") String customerId) {
        return customerService.getCustomer(customerId);
    }

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable("id") String customerId) {
        customerService.deleteCustomer(customerId);
    }

    @PostMapping("/{customerId}/change-password")
    public ResponseEntity<?> changePassword(
            @PathVariable String customerId,
            @RequestBody PasswordChangeRequest request) {
        boolean success = customerService.changePassword(customerId, request);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Current password is incorrect");
        }
    }
}