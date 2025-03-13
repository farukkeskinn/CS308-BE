package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.ShoppingCart;
import edu.sabanciuniv.projectbackend.repositories.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public List<ShoppingCart> getAllShoppingCarts() {
        return shoppingCartRepository.findAll();
    }

    public ShoppingCart getShoppingCart(String cartId) {
        return shoppingCartRepository.findById(cartId).orElse(null);
    }

    public ShoppingCart saveShoppingCart(ShoppingCart cart) {
        return shoppingCartRepository.save(cart);
    }

    public void deleteShoppingCart(String cartId) {
        shoppingCartRepository.deleteById(cartId);
    }
}
