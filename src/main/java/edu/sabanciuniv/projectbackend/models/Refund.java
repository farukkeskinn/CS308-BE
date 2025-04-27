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

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @OneToOne
    @JoinColumn(name = "order_item_id", nullable = false, unique = true)
    private OrderItem orderItem;

    // ----- GETTERS -----
    public String getRefundId() { return refundId; }
    public LocalDateTime getRequestDate() { return requestDate; }
    public String getRefundStatus() { return refundStatus; }
    public Double getRefundAmount() { return refundAmount; }
    public Order getOrder() { return order; }
    public OrderItem getOrderItem() { return orderItem; }

    // ----- SETTERS (EKLENDİ) -----
    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public void setRefundAmount(Double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
}
