package edu.sabanciuniv.projectbackend.services;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public void initShoppingCartsForExistingCustomers() {
        var allCustomers = customerRepository.findAll();

        for (Customer detachedCustomer : allCustomers) {
            // Hibernate managed instance kullan
            Customer customer = customerRepository.findById(detachedCustomer.getCustomerId()).orElse(null);
            if (customer == null) continue;

            if (shoppingCartRepository.findByCustomerId(customer.getCustomerId()) == null) {
                ShoppingCart cart = new ShoppingCart();
                cart.setCartId(customer.getCustomerId());
                cart.setCartStatus("EMPTY");
                cart.setCustomer(customer);
                shoppingCartRepository.save(cart);
            }

        }
    }

}