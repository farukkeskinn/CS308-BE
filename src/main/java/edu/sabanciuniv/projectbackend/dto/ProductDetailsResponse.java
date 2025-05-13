package edu.sabanciuniv.projectbackend.dto;

import edu.sabanciuniv.projectbackend.models.Product;

import java.util.List;

public class ProductDetailsResponse {
    private String productId;
    private String name;
    private String model;
    private String serialNumber;
    private String description;
    private Integer quantity;
    private Double price;
    private Integer warrantyStatus;
    private String distributor;
    private String imageUrl;
    private Boolean discounted;
    private Integer discountPercentage;
    private Double discountedPrice;
    private Integer itemSold;
    private String categoryName;
    private List<ReviewResponse> reviews;

    public ProductDetailsResponse(Product product, List<ReviewResponse> reviews) {
        this.productId = product.getProductId();
        this.name = product.getName();
        this.model = product.getModel();
        this.serialNumber = product.getSerialNumber();
        this.description = product.getDescription();
        this.quantity = product.getQuantity() != null ? product.getQuantity() : 0;
        this.price = product.getPrice() != null ? product.getPrice() : 0.0;
        this.warrantyStatus = product.getWarranty_status() != null ? product.getWarranty_status() : 0;
        this.distributor = product.getDistributor() != null ? product.getDistributor() : "N/A";
        this.imageUrl = product.getImage_url() != null ? product.getImage_url() : "";
        this.discounted = product.getDiscounted() != null ? product.getDiscounted() : false;
        this.discountPercentage = product.getDiscountPercentage() != null ? product.getDiscountPercentage() : 0;
        this.discountedPrice = product.getDiscountedPrice() != null ? product.getDiscountedPrice() : this.price;
        this.itemSold = product.getItemSold() != null ? product.getItemSold() : 0;
        this.categoryName = (product.getCategory() != null && product.getCategory().getCategoryName() != null)
                ? product.getCategory().getCategoryName()
                : "Uncategorized";
        this.reviews = reviews;
    }

    public String getName() { return name; }
    public String getProductId() { return productId; }
    public String getCategoryName() { return categoryName; }
    public Boolean getDiscounted() { return discounted; }
    public Integer getDiscountPercentage() { return discountPercentage; }
    public Double getDiscountedPrice() { return discountedPrice; }
    public Integer getItemSold() { return itemSold; }
    public String getModel() { return model; }
    public String getSerialNumber() { return serialNumber; }
    public String getDescription() { return description; }
    public Integer getQuantity() { return quantity; }
    public Double getPrice() { return price; }
    public Integer getWarrantyStatus() { return warrantyStatus; }
    public String getDistributor() { return distributor; }
    public String getImageUrl() { return imageUrl; }
    public List<ReviewResponse> getReviews() { return reviews; }
}