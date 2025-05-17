package edu.sabanciuniv.projectbackend.services;
import org.springframework.transaction.annotation.Transactional;
import edu.sabanciuniv.projectbackend.models.ShoppingCart;
import edu.sabanciuniv.projectbackend.models.ShoppingCartItem;
import edu.sabanciuniv.projectbackend.repositories.ShoppingCartItemRepository;
import edu.sabanciuniv.projectbackend.repositories.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ShoppingCartItemRepository shoppingCartItemRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
    }

    public List<ShoppingCart> getAllShoppingCarts() {
        return shoppingCartRepository.findAll();
    }

    public ShoppingCart getShoppingCart(String cartId) {
        return shoppingCartRepository.findById(cartId).orElse(null);
    }

    @Transactional
    public ShoppingCart saveShoppingCart(ShoppingCart cart) {
        return shoppingCartRepository.save(cart);
    }

    @Transactional
    public void deleteShoppingCart(String cartId) {
        shoppingCartRepository.deleteById(cartId);
    }

    public ShoppingCart getCartByUsername(String username) {
        return shoppingCartRepository.findByCustomerEmail(username);
    }

    @Transactional
    public void clearCart(String username) {
        ShoppingCart cart = shoppingCartRepository.findByCustomerEmail(username);
        if (cart != null && !cart.getShoppingCartItems().isEmpty()) {
            // 🔥 ShoppingCartItem'ları topluca sil
            shoppingCartItemRepository.deleteAll(cart.getShoppingCartItems());

            // 🧹 Java tarafındaki listeyi de temizle
            cart.getShoppingCartItems().clear();

            // 💾 Güncel halini veritabanına kaydet
            shoppingCartRepository.save(cart);
        }
    }



}
