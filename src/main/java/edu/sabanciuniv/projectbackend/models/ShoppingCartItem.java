package edu.sabanciuniv.projectbackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "shoppingcart_items")
public class ShoppingCartItem {

    @Id
    @Column(name = "shopping_cart_item_id", columnDefinition = "CHAR(36)")
    private String shoppingCartItemId;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonBackReference
    private ShoppingCart shoppingCart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties({"stock", "description", "category"}) // varsa ama istemiyorsan
    private Product product;

    // Getters and Setters
    public String getShoppingCartItemId() { return shoppingCartItemId; }
    public ShoppingCart getShoppingCart() { return shoppingCart; }
    public Product getProduct() { return product; }
    public Integer getQuantity() { return quantity; }

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
