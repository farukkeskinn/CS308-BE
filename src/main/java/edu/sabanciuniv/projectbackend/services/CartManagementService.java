package edu.sabanciuniv.projectbackend.services;

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
        // 1) Is this a GUEST?
        if (request.getCustomerId() == null || request.getCustomerId().isBlank()) {
            // Not saving to DB -> GUEST flow
            // [*TO BE ADDED*] return a 'dummy' cart
            ShoppingCart dummyCart = new ShoppingCart();
            dummyCart.setCartId("GUEST_FLOW"); // special ID
            return dummyCart;
        }

        // 2) USER (find customer)
        var customer = customerService.getCustomerById(request.getCustomerId());
        if (customer == null) {
            // Customer not found -> return null
            return null;
        }

        // 3) Find or create cart
        ShoppingCart cart = cartRepository.findByCustomer(customer);
        if (cart == null) {
            cart = new ShoppingCart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCartStatus("ACTIVE");
            cart.setCustomer(customer);
            cartRepository.save(cart);
        }

        // 4) Find product
        var product = productService.getProductById(request.getProductId());
        if (product == null) {
            // Product not found -> return null
            return null;
        }

        // 5) Is this product already in cart?
        ShoppingCartItem existingItem =
                cartItemRepository.findByCartIdAndProductId(cart.getCartId(), product.getProductId());

        if (existingItem != null) {
            // Existing item
            int newQuantity = existingItem.getQuantity() + request.getQuantity();
            // STOCK CHECK
            if (newQuantity > product.getStock()) {
                // NOT ENOUGH STOCK -> JUST return null
                return null;
            }
            // Stock sufficient -> increase item quantity
            existingItem.setQuantity(newQuantity);
            cartItemRepository.save(existingItem);

            // DECREASE STOCK
            product.setStock(product.getStock() - request.getQuantity());
            productService.saveProduct(product);

        } else {
            // New item
            if (request.getQuantity() > product.getStock()) {
                // NO STOCK -> return null
                return null;
            }

            // Add new row
            ShoppingCartItem newItem = new ShoppingCartItem();
            newItem.setShoppingCartItemId(UUID.randomUUID().toString());
            newItem.setShoppingCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            cartItemRepository.save(newItem);

            // DECREASE STOCK
            product.setStock(product.getStock() - request.getQuantity());
            productService.saveProduct(product);
        }

        return cart; // If stock is sufficient, item is added; otherwise, return null
    }

    /**
     * 2) Remove a single item (by itemId) from cart
     */
    public void removeItemFromCart(String itemId) {
        // 1) Find item
        ShoppingCartItem item = cartItemRepository.findById(itemId).orElse(null);
        if (item == null) {
            return;
        }

        // 2) Return quantity to stock
        Product product = item.getProduct();
        if (product != null) {
            int addBack = item.getQuantity();
            product.setStock(product.getStock() + addBack);
            productService.saveProduct(product);
        }

        // 3) Delete item
        cartItemRepository.deleteById(itemId);
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

        // 1) Return each item's quantity to stock
        for (ShoppingCartItem item : items) {
            Product product = item.getProduct();
            if (product != null) {
                product.setStock(product.getStock() + item.getQuantity());
                productService.saveProduct(product);
            }
        }

        // 2) Delete items from DB
        // -> Method A: deleteById one by one
        for (ShoppingCartItem item : items) {
            cartItemRepository.deleteById(item.getShoppingCartItemId());
        }

        // 3) Clear item list in memory as well
        items.clear();
        cartRepository.save(cart);
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
                int mergedQty = existing.getQuantity() + guestItem.getQuantity();
                // STOCK CHECK
                if (mergedQty > product.getStock()) {
                    // INSUFFICIENT STOCK -> return null, cancel merge
                    return null;
                }
                existing.setQuantity(mergedQty);
                cartItemRepository.save(existing);

                product.setStock(product.getStock() - guestItem.getQuantity());
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

                product.setStock(product.getStock() - guestItem.getQuantity());
                productService.saveProduct(product);
            }
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

    public void removePartialQuantity(String itemId, int quantityToRemove) {
        // 1) Find item
        ShoppingCartItem item = cartItemRepository.findById(itemId).orElse(null);
        if (item == null) {
            return;
        }

        // [ADDED] Find product
        Product product = item.getProduct();
        if (product == null) {
            return;
        }

        int currentQty = item.getQuantity();
        int newQty = currentQty - quantityToRemove;

        if (newQty <= 0) {
            // [ADDED] Return all quantity to stock
            product.setStock(product.getStock() + currentQty);
            productService.saveProduct(product);

            // Delete item completely
            cartItemRepository.deleteById(itemId);

        } else {
            // [ADDED] Partial decrease -> return quantityToRemove to stock
            product.setStock(product.getStock() + quantityToRemove);
            productService.saveProduct(product);

            // Update item quantity
            item.setQuantity(newQty);
            cartItemRepository.save(item);
        }
    }
}
