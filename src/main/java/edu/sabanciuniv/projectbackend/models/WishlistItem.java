package edu.sabanciuniv.projectbackend.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
@Entity
@Table(name = "wishlist_items")
public class WishlistItem {
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

    public void setWishlist(Wishlist wishlist) {
        this.wishlist = wishlist;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setWishlistItemId(String wishlistItemId) {
        this.wishlistItemId = wishlistItemId;
    }

}

