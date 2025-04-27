package edu.sabanciuniv.projectbackend.dto;

import java.util.List;

public class MergeCartRequest {

    private String customerId;
    private List<GuestCartItem> guestItems;

    public String getCustomerId() {
        return customerId;
    }

    public List<GuestCartItem> getGuestItems() {
        return guestItems;
    }

    public static class GuestCartItem {
        private String productId;
        private int quantity;

        public String getProductId() {
            return productId;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
