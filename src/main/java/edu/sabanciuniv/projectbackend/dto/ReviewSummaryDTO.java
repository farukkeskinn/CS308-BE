package edu.sabanciuniv.projectbackend.dto;

import java.time.LocalDateTime;

public class ReviewSummaryDTO {
    private String reviewId;
    private int rating;
    private String comment;
    private String approvalStatus;
    private LocalDateTime reviewDate;

    public ReviewSummaryDTO(String reviewId, int rating, String comment, String approvalStatus, LocalDateTime reviewDate) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.comment = comment;
        this.approvalStatus = approvalStatus;
        this.reviewDate = reviewDate;
    }

    public String getReviewId() { return reviewId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public String getApprovalStatus() { return approvalStatus; }
    public LocalDateTime getReviewDate() { return reviewDate; }
}
