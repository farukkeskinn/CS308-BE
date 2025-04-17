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
     * Adds an item to the cart.
     * Guest → returns dummy cart with ID "GUEST_FLOW", nothing saved to DB.
     * If stock not enough → returns null, with message.
     */
    @PostMapping("/add-item")
    public ResponseEntity<?> addItemToCart(@RequestBody AddItemRequest request) {
        ShoppingCart cart = cartManagementService.addItemToCart(request);

        if (cart == null) {
            return ResponseEntity.ok(Map.of("message", "There is no enough stock"));
        }

        if ("GUEST_FLOW".equals(cart.getCartId())) {
            return ResponseEntity.ok(Map.of("message", "guest flow, not saving to DB"));
        }

        return ResponseEntity.ok(cart);
    }

    /**
     * Merges guest items with logged-in user's cart.
     * Returns flag to clear localStorage after success.
     */
    @PostMapping("/merge-guest-cart")
    public ResponseEntity<?> mergeGuestCart(@RequestBody MergeCartRequest request) {
        ShoppingCart cart = cartManagementService.mergeGuestCart(request);

        if (cart == null) {
            return ResponseEntity.ok(Map.of(
                    "message", "There is no enough stock",
                    "clearGuestCart", false
            ));
        }

        return ResponseEntity.ok(Map.of(
                "message", "merge success",
                "clearGuestCart", true,
                "cart", cart
        ));
    }

    @DeleteMapping("/remove-item/{itemId}")
    public void removeItemFromCart(@PathVariable String itemId) {
        cartManagementService.removeItemFromCart(itemId);
    }

    @DeleteMapping("/clear-cart/{cartId}")
    public void clearCart(@PathVariable String cartId) {
        cartManagementService.clearCart(cartId);
    }

    @GetMapping("/calculate-total/{cartId}")
    public Double calculateTotal(@PathVariable String cartId) {
        return cartManagementService.calculateCartTotal(cartId);
    }

    @PatchMapping("/remove-item-quantity")
    public void removePartialQuantity(@RequestBody RemovePartialQuantityRequest request) {
        cartManagementService.removePartialQuantity(request.getItemId(), request.getQuantity());
    }
}
