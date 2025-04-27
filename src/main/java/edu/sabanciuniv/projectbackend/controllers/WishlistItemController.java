package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.models.WishlistItem;
import edu.sabanciuniv.projectbackend.services.WishlistItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist-items")
@CrossOrigin(origins = "http://localhost:3000")
public class WishlistItemController {

    private final WishlistItemService wishlistItemService;

    public WishlistItemController(WishlistItemService wishlistItemService) {
        this.wishlistItemService = wishlistItemService;
    }

    @GetMapping
    public List<WishlistItem> getAllWishlistItems() {
        return wishlistItemService.getAllWishlistItems();
    }

    @GetMapping("/{id}")
    public WishlistItem getWishlistItemById(@PathVariable("id") String wishlistItemId) {
        return wishlistItemService.getWishlistItemById(wishlistItemId);
    }

    @PostMapping
    public WishlistItem createWishlistItem(@RequestBody WishlistItem item) {
        return wishlistItemService.saveWishlistItem(item);
    }

    @DeleteMapping("/{id}")
    public void deleteWishlistItem(@PathVariable("id") String wishlistItemId) {
        wishlistItemService.deleteWishlistItem(wishlistItemId);
    }
}
