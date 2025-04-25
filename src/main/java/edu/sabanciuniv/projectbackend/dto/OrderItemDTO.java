package edu.sabanciuniv.projectbackend.dto;

public class OrderItemDTO {
    private String productId;
    private String productName;
    private double priceAtPurchase;
    private int quantity;

    public OrderItemDTO(String productId, String productName, double priceAtPurchase, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.priceAtPurchase = priceAtPurchase;
        this.quantity = quantity;
    }

    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getPriceAtPurchase() { return priceAtPurchase; }
    public int getQuantity() { return quantity; }
}
