package edu.sabanciuniv.projectbackend.models;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(name = "discounted")
    private Boolean discounted = false;

    @Column(name = "discount_percentage")
    private Integer discountPercentage;

    @Column(name = "discounted_price")
    private Double discountedPrice;

    @Column(name = "published")
    private Boolean published = false;

    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    private Integer quantity;
    private Integer warranty_status; // or an enum

    private String distributor;
    private String image_url; // or imageUrl

    // Relationship to Category
    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    // e.g., One product can have many reviews (if you want direct mapping):
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnoreProperties("product")
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

    public Double getDiscountedPrice() { return this.discountedPrice; }

    public void setDiscountedPrice() { this.discountedPrice = discountedPrice; }

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

    public void setModel(String model) {
        this.model = model;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public void setDiscounted(Boolean discounted) {
        this.discounted = discounted;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public Boolean getPublished() {
        return published != null ? published : false;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setWarranty_status(Integer warranty_status) {
        this.warranty_status = warranty_status;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}

