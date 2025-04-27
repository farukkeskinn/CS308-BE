package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Wishlist;
import edu.sabanciuniv.projectbackend.repositories.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    public WishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public List<Wishlist> getAllWishlists() {
        return wishlistRepository.findAll();
    }

    public Wishlist getWishlistById(String wishlistId) {
        return wishlistRepository.findById(wishlistId).orElse(null);
    }

    public Wishlist saveWishlist(Wishlist wishlist) {
        return wishlistRepository.save(wishlist);
    }

    public void deleteWishlist(String wishlistId) {
        wishlistRepository.deleteById(wishlistId);
    }
}
