package edu.sabanciuniv.projectbackend.dto;

public class AddItemRequest {

    // If the user is logged in/registered, customerId will be provided
    // If guest, this will be null or an empty string
    private String customerId;

    // ID of the product to be added
    private String productId;

    // How many?
    private int quantity;

    public String getCustomerId() {
        return customerId;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
