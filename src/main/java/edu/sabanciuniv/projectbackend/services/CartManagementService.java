package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Customer;
import edu.sabanciuniv.projectbackend.dto.AddItemRequest;
import edu.sabanciuniv.projectbackend.dto.MergeCartRequest;
import edu.sabanciuniv.projectbackend.models.ShoppingCart;
import edu.sabanciuniv.projectbackend.models.ShoppingCartItem;
import edu.sabanciuniv.projectbackend.models.Product;
import edu.sabanciuniv.projectbackend.repositories.ShoppingCartRepository;
import edu.sabanciuniv.projectbackend.repositories.ShoppingCartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.UUID;

import java.util.UUID;

@Service
@Transactional
public class CartManagementService {

    private final ShoppingCartRepository cartRepository;
    private final ShoppingCartItemRepository cartItemRepository;

    // If your project includes "CustomerService" / "ProductService", you can inject them here
    private final CustomerService customerService;
    private final ProductService productService;

    public CartManagementService(ShoppingCartRepository cartRepository,
                                 ShoppingCartItemRepository cartItemRepository,
                                 CustomerService customerService,
                                 ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.customerService = customerService;
        this.productService = productService;
    }

    /**
     * 1) Add item to cart (with stock control).
     *    - If guest (customerId = null/blank), do not save to DB, return a dummy cart.
     *    - If user, find or create the cart in DB.
     *        - If item already exists -> increase quantity -> if stock is insufficient, return error
     *        - If new item -> check stock, if insufficient return error, otherwise add
     */
    public ShoppingCart addItemToCart(AddItemRequest request) {
        // 1) GUEST flow
        if (request.getCustomerId() == null || request.getCustomerId().isBlank()) {
            ShoppingCart dummyCart = new ShoppingCart();
            dummyCart.setCartId("GUEST_FLOW");
            return dummyCart;
        }

        // 2) Get customer
        var customer = customerService.getCustomerById(request.getCustomerId());
        if (customer == null) {
            return null;
        }

        // 3) Get or create cart
        ShoppingCart cart = cartRepository.findByCustomer(customer);
        if (cart == null) {
            cart = new ShoppingCart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCartStatus("EMPTY");
            cart.setCustomer(customer);
            cartRepository.save(cart);
        }

        // 4) Get product
        var product = productService.getProductById(request.getProductId());
        if (product == null) {
            return null;
        }

        // 5) Stock check
        ShoppingCartItem existingItem = cartItemRepository
                .findByCartIdAndProductId(cart.getCartId(), product.getProductId());

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + request.getQuantity();
            if (newQuantity > product.getStock()) {
                return null;
            }
            existingItem.setQuantity(newQuantity);
            cartItemRepository.save(existingItem);


        } else {
            if (request.getQuantity() > product.getStock()) {
                return null;
            }

            ShoppingCartItem newItem = new ShoppingCartItem();
            newItem.setShoppingCartItemId(UUID.randomUUID().toString());
            newItem.setShoppingCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            cartItemRepository.save(newItem);

        }

        // ✅ Fix: Kart durumu güncelleniyor
        long itemCount = cartItemRepository.countByShoppingCart_CartId(cart.getCartId());
        if (itemCount > 0 && !"ACTIVE".equals(cart.getCartStatus())) {
            cart.setCartStatus("ACTIVE");
            cartRepository.save(cart);
        }

        return cart;
    }



    public void logoutAndClearCart(String customerId) {
        var customer = customerService.getCustomerById(customerId);
        if (customer == null) return;

        var cart = cartRepository.findByCustomer(customer);
        if (cart == null) return;

        clearCart(cart.getCartId());
    }


    /**
     * 2) Remove a single item (by itemId) from cart
     */
    public void removeItemFromCart(String itemId) {
        // 1) Find item
        ShoppingCartItem item = cartItemRepository.findById(itemId).orElse(null);
        if (item == null) return;

        // 2) Return stock
        Product product = item.getProduct();

        // 3) Get cart ID
        String cartId = item.getShoppingCart().getCartId();

        // 4) Delete item
        cartItemRepository.deleteById(itemId);

        // 5) Check cart status AFTER deletion
        ShoppingCart cart = cartRepository.findById(cartId).orElse(null);
        if (cart != null) {
            boolean isNowEmpty = cartItemRepository.findAll().stream()
                    .filter(i -> i.getShoppingCart().getCartId().equals(cartId))
                    .count() == 0;

            cart.setCartStatus(isNowEmpty ? "EMPTY" : "ACTIVE");
            cartRepository.save(cart);
        }
    }



    /**
     * 3) Completely clear a specific cart
     */
    public void clearCart(String cartId) {
        ShoppingCart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return;
        }

        // Get all items
        List<ShoppingCartItem> items = cart.getShoppingCartItems();


        // 2) Delete items from DB
        // -> Method A: deleteById one by one
        for (ShoppingCartItem item : items) {
            cartItemRepository.deleteById(item.getShoppingCartItemId());
        }

        // 3) Clear item list in memory as well
        items.clear();
        cartRepository.save(cart);

        // En sona ekle
        long updatedCount = cartItemRepository.countByShoppingCart_CartId(cart.getCartId());
        String newStatus = (updatedCount == 0) ? "EMPTY" : "ACTIVE";
        if (!cart.getCartStatus().equals(newStatus)) {
            cart.setCartStatus(newStatus);
            cartRepository.save(cart);
        }

    }

    /**
     * 4) Merge guest items with the logged-in/registered user's cart
     *    - Stock is checked for each product. If insufficient -> return error.
     */
    public ShoppingCart mergeGuestCart(MergeCartRequest request) {
        // Find user
        var customer = customerService.getCustomerById(request.getCustomerId());

        if (customer == null) {
            return null;  // Not due to stock, but user not found -> return null
        }

        // Find or create cart
        ShoppingCart cart = cartRepository.findByCustomer(customer);
        if (cart == null) {
            cart = new ShoppingCart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCartStatus("ACTIVE");
            cart.setCustomer(customer);
            cartRepository.save(cart);
        }

        // Guest item list
        for (var guestItem : request.getGuestItems()) {
            var product = productService.getProductById(guestItem.getProductId());
            if (product == null) {
                continue; // skip this product
            }

            // Already in cart?
            ShoppingCartItem existing = cartItemRepository
                    .findByCartIdAndProductId(cart.getCartId(), product.getProductId());

            if (existing != null) {
                int mergedQty = existing.getQuantity();
                // STOCK CHECK
                if (mergedQty > product.getStock()) {
                    // INSUFFICIENT STOCK -> return null, cancel merge
                    return null;
                }
                existing.setQuantity(mergedQty);
                cartItemRepository.save(existing);

                productService.saveProduct(product);
            } else {
                if (guestItem.getQuantity() > product.getStock()) {
                    return null; // no stock
                }

                var newItem = new ShoppingCartItem();
                newItem.setShoppingCartItemId(UUID.randomUUID().toString());
                newItem.setShoppingCart(cart);
                newItem.setProduct(product);
                newItem.setQuantity(guestItem.getQuantity());
                cartItemRepository.save(newItem);

                productService.saveProduct(product);
            }
        }

        // Eğer sepete en az 1 ürün eklendiyse, durumu ACTIVE yap
        if (!cart.getShoppingCartItems().isEmpty()) {
            cart.setCartStatus("ACTIVE");
            cartRepository.save(cart);
        }

        return cart; // merge if stock is sufficient
    }

    /**
     * 5) Calculate total price of the cart
     *    (sum of product price * quantity)
     */
    public Double calculateCartTotal(String cartId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) return 0.0;

        double total = 0.0;
        for (var item : cart.getShoppingCartItems()) {
            double price = item.getProduct().getPrice(); // assuming product.getPrice()
            total += price * item.getQuantity();
        }
        return total;
    }



    public Customer getCustomerById(String customerId) {
        return customerService.getCustomerById(customerId);
    }

    public ShoppingCart getCartByCustomer(Customer customer) {
        return cartRepository.findByCustomer(customer);
    }


    public void removePartialQuantity(String itemId, int quantityToRemove) {
        ShoppingCartItem item = cartItemRepository.findById(itemId).orElse(null);
        if (item == null) return;

        Product product = item.getProduct();
        if (product == null) return;

        int currentQty = item.getQuantity();
        int newQty = currentQty;
        String cartId = item.getShoppingCart().getCartId();

        if (newQty <= 0) {
            // Remove completely
            productService.saveProduct(product);
            cartItemRepository.deleteById(itemId);
        } else {
            // Decrease partially
            productService.saveProduct(product);
            item.setQuantity(newQty);
            cartItemRepository.save(item);
        }

        // Final check for cart status
        ShoppingCart cart = cartRepository.findById(cartId).orElse(null);
        if (cart != null) {
            boolean isNowEmpty = cartItemRepository.findAll().stream()
                    .filter(i -> i.getShoppingCart().getCartId().equals(cartId))
                    .count() == 0;

            cart.setCartStatus(isNowEmpty ? "EMPTY" : "ACTIVE");
            cartRepository.save(cart);
        }
    }

}