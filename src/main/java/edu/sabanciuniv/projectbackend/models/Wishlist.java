package edu.sabanciuniv.projectbackend.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wishlists")
public class Wishlist {
    @Version
    @Column(name = "version", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long version = 0L;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (version == null) {
            version = 0L;
        }
    }

    public Long getVersion() {
        return version != null ? version : 0L;
    }

    public void setVersion(Long version) {
        this.version = version != null ? version : 0L;
    }


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
    @JsonIgnoreProperties("wishlist")
    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<WishlistItem> wishlistItems = new ArrayList<>();

    // Constructors, Getters, Setters
    public String getWishlistId() {
        return wishlistId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<WishlistItem> getWishlistItems() {
        return wishlistItems;
    }

    public void setWishlistId(String wishlistId) {
        this.wishlistId = wishlistId;
    }

    public void setWishlistStatus(String wishlistStatus) {
        this.wishlistStatus = wishlistStatus;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setWishlistItems(List<WishlistItem> wishlistItems) {
        this.wishlistItems = wishlistItems;
    }
}

