package com.aloha.zootopia.domain;

import java.time.LocalDateTime;

public class Review {
    private Long id;
    private Long productId;
    private Long userId;
    private String userName;
    private Integer rating;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    // Default constructor
    public Review() {}

    // Constructor
    public Review(Long productId, Long userId, String userName, Integer rating, String content) {
        this.productId = productId;
        this.userId = userId;
        this.userName = userName;
        this.rating = rating;
        this.content = content;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", productId=" + productId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", rating=" + rating +
                ", content='" + content + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
