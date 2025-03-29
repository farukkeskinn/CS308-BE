package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.dto.AddItemRequest;
import edu.sabanciuniv.projectbackend.dto.MergeCartRequest;
import edu.sabanciuniv.projectbackend.dto.RemovePartialQuantityRequest;
import edu.sabanciuniv.projectbackend.models.ShoppingCart;
import edu.sabanciuniv.projectbackend.services.CartManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart-management")
@CrossOrigin(origins = "http://localhost:3000")
public class CartManagementController {

    private final CartManagementService cartManagementService;

    public CartManagementController(CartManagementService cartManagementService) {
        this.cartManagementService = cartManagementService;
    }

    /**
     * Adds an item to the cart (or increases the quantity if it already exists).
     * If guest -> customerId is null -> does not save to DB (localStorage).
     * If there is not enough stock -> returns null -> JSON: "There is no enough stock".
     */
    @PostMapping("/add-item")
    public ResponseEntity<?> addItemToCart(@RequestBody AddItemRequest request) {
        ShoppingCart cart = cartManagementService.addItemToCart(request);

        if (cart == null) {
            // Here 'null' => means INSUFFICIENT STOCK
            return ResponseEntity.ok(Map.of("message","There is no enough stock"));
        }

        if ("GUEST_FLOW".equals(cart.getCartId())) {
            // This cart belongs to GUEST scenario
            // No DB record -> front-end uses localStorage
            return ResponseEntity.ok(Map.of("message","guest flow, not saving to DB"));
        }

        // STOCK IS SUFFICIENT + USER
        return ResponseEntity.ok(cart);
    }


    /**
     * Removes a single item (by itemId) from the cart.
     */
    @DeleteMapping("/remove-item/{itemId}")
    public void removeItemFromCart(@PathVariable String itemId) {
        cartManagementService.removeItemFromCart(itemId);
    }

    /**
     * Completely clears the given cart (by cartId), removing all items.
     */
    @DeleteMapping("/clear-cart/{cartId}")
    public void clearCart(@PathVariable String cartId) {
        cartManagementService.clearCart(cartId);
    }

    /**
     * Merges the guest cart (from localStorage) with the logged-in user's cart.
     * If there is not enough stock -> returns null -> message response.
     */
    @PostMapping("/merge-guest-cart")
    public ResponseEntity<?> mergeGuestCart(@RequestBody MergeCartRequest request) {
        ShoppingCart cart = cartManagementService.mergeGuestCart(request);
        if (cart == null) {
            // Insufficient stock or some other issue
            return ResponseEntity.ok(Map.of("message", "There is no enough stock"));
        }
        return ResponseEntity.ok(cart);
    }

    /**
     * Calculates the total cart value (price * quantity).
     */
    @GetMapping("/calculate-total/{cartId}")
    public Double calculateTotal(@PathVariable String cartId) {
        return cartManagementService.calculateCartTotal(cartId);
    }

    /**
     * Removes a partial quantity of an item from the cart.
     */
    @PatchMapping("/remove-item-quantity")
    public void removePartialQuantity(@RequestBody RemovePartialQuantityRequest request) {
        cartManagementService.removePartialQuantity(request.getItemId(), request.getQuantity());
    }
}
