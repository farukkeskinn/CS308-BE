package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.WishlistItem;
import edu.sabanciuniv.projectbackend.repositories.WishlistItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistItemService {

    private final WishlistItemRepository wishlistItemRepository;

    public WishlistItemService(WishlistItemRepository wishlistItemRepository) {
        this.wishlistItemRepository = wishlistItemRepository;
    }

    public List<WishlistItem> getAllWishlistItems() {
        return wishlistItemRepository.findAll();
    }

    public WishlistItem getWishlistItemById(String wishlistItemId) {
        return wishlistItemRepository.findById(wishlistItemId).orElse(null);
    }

    public WishlistItem saveWishlistItem(WishlistItem item) {
        return wishlistItemRepository.save(item);
    }

    public void deleteWishlistItem(String wishlistItemId) {
        wishlistItemRepository.deleteById(wishlistItemId);
    }
}
