package edu.sabanciuniv.projectbackend.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shoppingcarts")
public class ShoppingCart {

    @Id
    @Column(name = "cart_id", columnDefinition = "CHAR(36)")
    private String cartId;

    @Column(name = "cart_status", nullable = false)
    private String cartStatus;

    // One-to-one with Customer
    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    @JsonIgnore
    private Customer customer;

    // One cart can have many items
    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL)
    private List<ShoppingCartItem> shoppingCartItems = new ArrayList<>();

    // Constructors, Getters, Setters
    public String getCartId() {
        return cartId;
    }

    public String getCartStatus() {
        return cartStatus;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<ShoppingCartItem> getShoppingCartItems() {
        return shoppingCartItems;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public void setCartStatus(String cartStatus) {
        this.cartStatus = cartStatus;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setShoppingCartItems(List<ShoppingCartItem> shoppingCartItems) {
        this.shoppingCartItems = shoppingCartItems;
    }


}
