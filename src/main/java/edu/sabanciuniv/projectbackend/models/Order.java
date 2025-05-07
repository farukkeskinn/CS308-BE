package edu.sabanciuniv.projectbackend.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "order_id", columnDefinition = "CHAR(36)")
    private String orderId;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "order_status", nullable = false)
    private String orderStatus;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @Column(name = "invoice_link")
    private String invoiceLink;

    @Column(name = "refundable", nullable = false)
    private Boolean refundable = true; // Varsayılan olarak true

    @ManyToOne
    private SalesManager salesManager;

    // Relationship to Customer
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    // One order can have many order items
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    // Constructors, Getters, Setters
    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getInvoiceLink() {
        return invoiceLink;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Boolean getRefundable() {
        return refundable;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setInvoiceLink(String invoiceLink) {
        this.invoiceLink = invoiceLink;
    }

    public void setRefundable(Boolean refundable) {
        this.refundable = refundable;
    }
}