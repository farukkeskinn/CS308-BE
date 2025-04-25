// src/main/java/edu/sabanciuniv/projectbackend/dto/ReviewResponseDetail.java
package edu.sabanciuniv.projectbackend.dto;

import java.time.LocalDateTime;

public class ReviewResponseDetail {
    private String reviewId;
    private String customerId;
    private String productId;
    private int rating;
    private String comment;
    private LocalDateTime reviewDate;
    private String approvalStatus;

    public ReviewResponseDetail(String reviewId,
                                String customerId,
                                String productId,
                                int rating,
                                String comment,
                                LocalDateTime reviewDate,
                                String approvalStatus) {
        this.reviewId      = reviewId;
        this.customerId    = customerId;
        this.productId     = productId;
        this.rating        = rating;
        this.comment       = comment;
        this.reviewDate    = reviewDate;
        this.approvalStatus= approvalStatus;
    }

    public String getReviewId()      { return reviewId; }
    public String getCustomerId()    { return customerId; }
    public String getProductId()     { return productId; }
    public int getRating()           { return rating; }
    public String getComment()       { return comment; }
    public LocalDateTime getReviewDate() { return reviewDate; }
    public String getApprovalStatus(){ return approvalStatus; }
}
