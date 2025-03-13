package edu.sabanciuniv.projectbackend.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "shoppingcart_items")
public class ShoppingCartItem {

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
}

