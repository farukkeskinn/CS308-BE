package edu.sabanciuniv.projectbackend.dto;
import edu.sabanciuniv.projectbackend.models.Product;

public class ProductPublishedResponse {
    private String productId;
    private String name;
    private String model;
    private String serialNumber;
    private String description;
    private Integer stock;
    private Double price;
    private Boolean discounted;
    private Integer discountPercentage;
    private Double discountedPrice;
    private String distributor;
    private String image_url;
    private Integer warrantyStatus;

    public ProductPublishedResponse() {}

    public ProductPublishedResponse(Product p) {
        this.productId = p.getProductId();
        this.name = p.getName();
        this.model = p.getModel();
        this.serialNumber = p.getSerialNumber();
        this.description = p.getDescription();
        this.stock = p.getQuantity() != null ? p.getQuantity() : 0;
        this.price = p.getPrice() != null ? p.getPrice() : 0.0;
        this.discounted = p.getDiscounted() != null ? p.getDiscounted() : false;
        this.discountPercentage = p.getDiscountPercentage() != null ? p.getDiscountPercentage() : 0;
        this.discountedPrice = p.getDiscountedPrice() != null ? p.getDiscountedPrice() : this.price;
        this.distributor = p.getDistributor() != null ? p.getDistributor() : "N/A";
        this.image_url = p.getImage_url() != null ? p.getImage_url() : "";
        this.warrantyStatus = p.getWarranty_status() != null ? p.getWarranty_status() : 0;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public String getModel() { return model; }
    public String getSerialNumber() { return serialNumber; }
    public String getDescription() { return description; }
    public Integer getStock() { return stock; }
    public Double getPrice() { return price; }
    public Boolean getDiscounted() { return discounted; }
    public Integer getDiscountPercentage() { return discountPercentage; }
    public Double getDiscountedPrice() { return discountedPrice; }
    public String getDistributor() { return distributor; }
    public String getImage_url() { return image_url; }
    public Integer getWarrantyStatus() { return warrantyStatus; }
}