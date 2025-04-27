package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Customer;
import edu.sabanciuniv.projectbackend.models.ShoppingCart;
import edu.sabanciuniv.projectbackend.repositories.CustomerRepository;
import edu.sabanciuniv.projectbackend.repositories.ShoppingCartRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class CartInitializerService {

    private final CustomerRepository customerRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    public CartInitializerService(CustomerRepository customerRepository,
                                  ShoppingCartRepository shoppingCartRepository) {
        this.customerRepository = customerRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @PostConstruct
    public void initShoppingCartsForExistingCustomers() {
        var allCustomers = customerRepository.findAll();
        for (Customer customer : allCustomers) {
            if (shoppingCartRepository.findByCustomer(customer) == null) {
                ShoppingCart cart = new ShoppingCart();
                cart.setCartId(customer.getCustomerId()); // ID eşitliği
                cart.setCartStatus("EMPTY");
                cart.setCustomer(customer);
                shoppingCartRepository.save(cart);
                System.out.println("Cart created for customer: " + customer.getCustomerId());
            }
        }
    }
}