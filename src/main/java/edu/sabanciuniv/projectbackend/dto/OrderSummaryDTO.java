package edu.sabanciuniv.projectbackend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderSummaryDTO {
    private String orderId;
    private Double totalPrice;
    private LocalDateTime orderDate;
    private String orderStatus;
    private String paymentStatus;
    private List<OrderItemDTO> orderItems;

    public OrderSummaryDTO(String orderId, Double totalPrice, LocalDateTime orderDate,
                           String orderStatus, String paymentStatus, List<OrderItemDTO> orderItems) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.orderItems = orderItems;
    }

    public String getOrderId() { return orderId; }
    public Double getTotalPrice() { return totalPrice; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public String getOrderStatus() { return orderStatus; }
    public String getPaymentStatus() { return paymentStatus; }
    public List<OrderItemDTO> getOrderItems() { return orderItems; }
}
