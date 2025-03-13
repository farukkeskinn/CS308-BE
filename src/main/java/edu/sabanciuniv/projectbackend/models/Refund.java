package edu.sabanciuniv.projectbackend.models;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refunds")
public class Refund {

    @Id
    @Column(name = "refund_id", columnDefinition = "CHAR(36)")
    private String refundId;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

    @Column(name = "refund_status", nullable = false)
    private String refundStatus;

    @Column(name = "refund_amount", nullable = false)
    private Double refundAmount;

    // Relationship to Order
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Relationship to OrderItem (unique constraint)
    @OneToOne
    @JoinColumn(name = "order_item_id", nullable = false, unique = true)
    private OrderItem orderItem;

    // Constructors, Getters, Setters
    public String getRefundId() {
        return refundId;
    }

    public Order getOrder() {
        return order;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public Double getRefundAmount() {
        return refundAmount;
    }
}
