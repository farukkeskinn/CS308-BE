package edu.sabanciuniv.projectbackend.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "shoppingcart_items")
public class ShoppingCartItem {

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
    @Column(name = "shopping_cart_item_id", columnDefinition = "CHAR(36)")
    private String shoppingCartItemId;

    @Column(nullable = false)
    private Integer quantity;

    // Relationship to ShoppingCart
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonIgnore
    private ShoppingCart shoppingCart;

    // Relationship to Product
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Constructors, Getters, Setters
    public String getShoppingCartItemId() {
        return shoppingCartItemId;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setShoppingCartItemId(String shoppingCartItemId) {
        this.shoppingCartItemId = shoppingCartItemId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
