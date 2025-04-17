package edu.sabanciuniv.projectbackend.dto;

public class ReviewResponse {
    private int rating;
    private String comment;
    private String approvalStatus;

    public ReviewResponse(int rating, String comment, String approvalStatus) {
        this.rating = rating;
        this.comment = comment;
        this.approvalStatus = approvalStatus;
    }

    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public String getApprovalStatus() { return approvalStatus; }
}