package edu.sabanciuniv.projectbackend.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "product_id", columnDefinition = "CHAR(36)")
    private String productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String model;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "item_sold")
    private Integer itemSold;

    private Double price;
    private Double cost;

    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    private Integer quantity;
    private Integer warranty_status; // or an enum

    private String distributor;
    private String image_url; // or imageUrl

    // Relationship to Category
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // e.g., One product can have many reviews (if you want direct mapping):
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Review> reviews = new ArrayList<>();

    // Constructors, Getters, Setters
    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getModel() {
        return model;
    }

    public String getDescription() {
        return description;
    }

    public Integer getItemSold() {
        return itemSold;
    }

    public Double getPrice() {
        return price;
    }

    public Double getCost() {
        return cost;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getWarranty_status() {
        return warranty_status;
    }

    public String getDistributor() {
        return distributor;
    }

    public String getImage_url() {
        return image_url;
    }

    public Category getCategory() {
        return category;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public Integer getStock() {
        // "quantity" will used as stock
        return this.quantity;
    }

    public void setStock(Integer stock) {
        this.quantity = stock;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setProductId(String productId) { this.productId = productId; }

    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public void setItemSold(Integer itemSold) {
        this.itemSold = itemSold;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

