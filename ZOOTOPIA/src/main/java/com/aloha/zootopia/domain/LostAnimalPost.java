package com.aloha.zootopia.domain;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class LostAnimalPost {
    private int postId;
    private String title;
    private String content;          
    private String lostLocation;
    private LocalDateTime lostTime;
    private String contactPhone;

    private int viewCount;
    private int likeCount;
    private int commentCount;

    private Date createdAt;
    private Date updatedAt;

    private String thumbnailUrl;

    private Long userId;
    private Users user;  

    private List<Comment> comments;

    
    private String tags;
    private List<Tag> tagList;
}
