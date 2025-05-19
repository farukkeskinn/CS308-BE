package edu.sabanciuniv.projectbackend.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "discounts")
public class Discount {

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
    @Column(name = "discount_id", columnDefinition = "CHAR(36)")
    private String discountId;

    @Column(name = "discount_rate", nullable = false)
    private Double discountRate;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    // Relationship to Product
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Constructors, Getters, Setters
    public String getDiscountId() {
        return discountId;
    }

    public Product getProduct() {
        return product;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }
}
