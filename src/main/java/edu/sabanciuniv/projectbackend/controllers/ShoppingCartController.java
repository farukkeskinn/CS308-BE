package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.models.ShoppingCart;
import edu.sabanciuniv.projectbackend.services.ShoppingCartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shopping-carts")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public List<ShoppingCart> getAllShoppingCarts() {
        return shoppingCartService.getAllShoppingCarts();
    }

    @GetMapping("/{id}")
    public ShoppingCart getShoppingCartById(@PathVariable("id") String cartId) {
        return shoppingCartService.getShoppingCart(cartId);
    }

    @PostMapping
    public ShoppingCart createShoppingCart(@RequestBody ShoppingCart cart) {
        return shoppingCartService.saveShoppingCart(cart);
    }

    @DeleteMapping("/{id}")
    public void deleteShoppingCart(@PathVariable("id") String cartId) {
        shoppingCartService.deleteShoppingCart(cartId);
    }
}
