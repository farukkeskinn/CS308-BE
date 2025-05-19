package edu.sabanciuniv.projectbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "refunds")
public class Refund {

    @Version
    @Column(name = "version", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long version = 0L;

    @Id
    @Column(name = "refund_id", columnDefinition = "CHAR(36)")
    private String refundId;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

    @Column(name = "refund_status", nullable = false)
    private String refundStatus;

    @Column(name = "refund_amount", nullable = false)
    private Double refundAmount;

    @Column(name = "reason")
    private String reason;

    @Column(name = "process_date")
    private LocalDateTime processDate;

    /* ──────────────────────────────────────── */
    @ManyToOne
    @JsonIgnore                                  // ✨ eklendi
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @OneToOne
    @JsonIgnore                                  // ✨ eklendi
    @JoinColumn(name = "order_item_id", nullable = false, unique = true)
    private OrderItem orderItem;
    /* ──────────────────────────────────────── */

    // ----- GETTERS -----
    public String getRefundId()              { return refundId; }
    public LocalDateTime getRequestDate()    { return requestDate; }
    public String getRefundStatus()          { return refundStatus; }
    public Double getRefundAmount()          { return refundAmount; }
    public Order getOrder()                  { return order; }
    public OrderItem getOrderItem()          { return orderItem; }
    public String getReason()                { return reason; }
    public LocalDateTime getProcessDate()    { return processDate; }

    // ----- SETTERS -----
    public void setRefundId(String refundId)                { this.refundId = refundId; }
    public void setRequestDate(LocalDateTime requestDate)    { this.requestDate = requestDate; }
    public void setRefundStatus(String refundStatus)         { this.refundStatus = refundStatus; }
    public void setRefundAmount(Double refundAmount)         { this.refundAmount = refundAmount; }
    public void setOrder(Order order)                        { this.order = order; }
    public void setOrderItem(OrderItem orderItem)            { this.orderItem = orderItem; }
    public void setReason(String reason)                     { this.reason = reason; }
    public void setProcessDate(LocalDateTime processDate)    { this.processDate = processDate; }
    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (version == null) {
            version = 0L;
        }
    }

    public Long getVersion() {
        return version != null ? version : 0L;
    }

    public void setVersion(Long version) {
        this.version = version != null ? version : 0L;
    }
}
