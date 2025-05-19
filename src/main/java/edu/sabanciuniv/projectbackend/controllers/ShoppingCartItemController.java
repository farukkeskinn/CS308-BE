package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.models.ShoppingCartItem;
import edu.sabanciuniv.projectbackend.services.ShoppingCartItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shopping-cart-items")
public class ShoppingCartItemController {

    private final ShoppingCartItemService shoppingCartItemService;

    public ShoppingCartItemController(ShoppingCartItemService shoppingCartItemService) {
        this.shoppingCartItemService = shoppingCartItemService;
    }

    @GetMapping
    public List<ShoppingCartItem> getAllShoppingCartItems() {
        return shoppingCartItemService.getAllShoppingCartItems();
    }

    @GetMapping("/{id}")
    public ShoppingCartItem getShoppingCartItemById(@PathVariable("id") String itemId) {
        return shoppingCartItemService.getShoppingCartItemById(itemId);
    }

    @PostMapping
    public ShoppingCartItem createShoppingCartItem(@RequestBody ShoppingCartItem item) {
        return shoppingCartItemService.saveShoppingCartItem(item);
    }

    @DeleteMapping("/{id}")
    public void deleteShoppingCartItem(@PathVariable("id") String itemId) {
        shoppingCartItemService.deleteShoppingCartItem(itemId);
    }
}
