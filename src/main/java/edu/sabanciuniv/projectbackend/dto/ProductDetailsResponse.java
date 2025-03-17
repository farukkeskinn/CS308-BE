package edu.sabanciuniv.projectbackend.dto;

import edu.sabanciuniv.projectbackend.models.Review;
import java.util.List;

public class ProductDetailsResponse {
    private String name;
    private String model;
    private String serialNumber;
    private String description;
    private Integer quantity;
    private Double price;
    private Integer warrantyStatus;
    private String distributor;
    private String imageUrl;
    private List<ReviewResponse> reviews;

    public ProductDetailsResponse(String name, String model, String serialNumber,
                                  String description, Integer quantity, Double price,
                                  Integer warrantyStatus, String distributor, String imageUrl,
                                  List<ReviewResponse> reviews) {
        this.name = name;
        this.model = model;
        this.serialNumber = serialNumber;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.warrantyStatus = warrantyStatus;
        this.distributor = distributor;
        this.imageUrl = imageUrl;
        this.reviews = reviews;
    }

    public String getName() { return name; }
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