package edu.sabanciuniv.projectbackend.dto;

public class RemovePartialQuantityRequest {

    private String itemId;
    private int quantity;  // -> "quantity"

    public String getItemId() { // -> "getItemId()"
        return itemId;
    }
    public int getQuantity() {  // -> "getQuantity()"
        return quantity;
    }
}


