package edu.sabanciuniv.projectbackend.dto;

import edu.sabanciuniv.projectbackend.models.WishlistItem;

public record WishlistItemDTO(String productId, String name, String imageUrl) {
    public static WishlistItemDTO fromEntity(WishlistItem item) {
        return new WishlistItemDTO(
                item.getProduct().getProductId(),
                item.getProduct().getName(),
                item.getProduct().getImage_url()
        );
    }
}
