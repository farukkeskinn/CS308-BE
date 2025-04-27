package edu.sabanciuniv.projectbackend.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wishlists")
public class Wishlist {

    @Id
    @Column(name = "wishlist_id", columnDefinition = "CHAR(36)")
    private String wishlistId;

    @Column(name = "wishlist_status", nullable = false)
    private String wishlistStatus;

    // One-to-one with Customer
    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    @JsonIgnore
    private Customer customer;

    // One wishlist can have many wishlist items
    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL)
    private List<WishlistItem> wishlistItems = new ArrayList<>();

    // Constructors, Getters, Setters
    public String getWishlistId() {
        return wishlistId;
    }

    public String getWishlistStatus() {
        return wishlistStatus;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<WishlistItem> getWishlistItems() {
        return wishlistItems;
    }
}

