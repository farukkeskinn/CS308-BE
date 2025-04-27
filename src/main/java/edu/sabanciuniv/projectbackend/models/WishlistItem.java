package edu.sabanciuniv.projectbackend.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
@Entity
@Table(name = "wishlist_items")
public class WishlistItem {

    @Id
    @Column(name = "wishlist_item_id", columnDefinition = "CHAR(36)")
    private String wishlistItemId;

    // Relationship to Wishlist
    @ManyToOne
    @JoinColumn(name = "wishlist_id", nullable = false)
    @JsonIgnore
    private Wishlist wishlist;

    // Relationship to Product
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    // Constructors, Getters, Setters
    public String getWishlistItemId() {
        return wishlistItemId;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }

    public Product getProduct() {
        return product;
    }
}

