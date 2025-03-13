package edu.sabanciuniv.projectbackend.models;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @Column(name = "customer_id", columnDefinition = "CHAR(36)")
    private String customerId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    // -- Relationships --
    // 1) One customer has many addresses:
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Address> addresses = new ArrayList<>();

    // 2) One customer can have many orders:
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    // 3) One customer can have one shopping cart:
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private ShoppingCart shoppingCart;

    // 4) One customer can have one wishlist:
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Wishlist wishlist;

    // 5) One customer can have many reviews:
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    // Constructors, Getters, Setters
    public String getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}