package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.ShoppingCartItem;
import edu.sabanciuniv.projectbackend.repositories.ShoppingCartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartItemService {

    private final ShoppingCartItemRepository shoppingCartItemRepository;

    public ShoppingCartItemService(ShoppingCartItemRepository shoppingCartItemRepository) {
        this.shoppingCartItemRepository = shoppingCartItemRepository;
    }

    public List<ShoppingCartItem> getAllShoppingCartItems() {
        return shoppingCartItemRepository.findAll();
    }

    public ShoppingCartItem getShoppingCartItemById(String itemId) {
        return shoppingCartItemRepository.findById(itemId).orElse(null);
    }

    public ShoppingCartItem saveShoppingCartItem(ShoppingCartItem item) {
        return shoppingCartItemRepository.save(item);
    }

    public void deleteShoppingCartItem(String itemId) {
        shoppingCartItemRepository.deleteById(itemId);
    }
}
