package edu.sabanciuniv.projectbackend.models;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @Column(name = "review_id", columnDefinition = "CHAR(36)")
    private String reviewId;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "approval_status", nullable = false)
    private String approvalStatus;

    // Relationship to Customer
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    // Relationship to Product
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    private Product product;

    // Constructors, Getters, Setters
    public String getReviewId() {
        return reviewId;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    @JsonProperty("productId")
    public String getProductId() {
        return product == null ? null : product.getProductId();
    }
}
