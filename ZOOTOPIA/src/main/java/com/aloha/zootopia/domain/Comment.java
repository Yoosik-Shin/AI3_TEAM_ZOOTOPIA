package com.aloha.zootopia.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    private Integer commentId;   
    private String content;      

    private Date createdAt;      
    private Date updatedAt;      
    private Boolean isDeleted;   

    private Integer userId;     
    private Integer postId;      

    
    private String nickname;     
    private String profileImg;   
}
