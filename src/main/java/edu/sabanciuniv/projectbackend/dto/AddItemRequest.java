package edu.sabanciuniv.projectbackend.dto;

import java.util.List;

public class AddItemRequest {

    private String customerId;
    private List<CartItemRequest> items;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<CartItemRequest> getItems() {
        return items;
    }

    public void setItems(List<CartItemRequest> items) {
        this.items = items;
    }
}
