package edu.sabanciuniv.projectbackend.models;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Version
    @Column(name = "version", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long version = 0L;

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

    @Id
    @Column(name = "payment_id", columnDefinition = "CHAR(36)")
    private String paymentId;

    @Column(name = "encrypted_credit_card_info", nullable = false)
    private String encryptedCreditCardInfo;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    // One-to-one with Order (unique constraint on order_id)
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Constructors, Getters, Setters
    public String getPaymentId() {
        return paymentId;
    }

    public Order getOrder() {
        return order;
    }

    public String getEncryptedCreditCardInfo() {
        return encryptedCreditCardInfo;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public Double getAmount() {
        return amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setEncryptedCreditCardInfo(String encryptedCreditCardInfo) {
        this.encryptedCreditCardInfo = encryptedCreditCardInfo;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}
