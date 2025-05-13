package edu.sabanciuniv.projectbackend.dto;

public class RefundRequest {
    private String orderId;
    private String orderItemId;
    private String reason;

    // Getters ve Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getOrderItemId() { return orderItemId; }
    public void setOrderItemId(String orderItemId) { this.orderItemId = orderItemId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}