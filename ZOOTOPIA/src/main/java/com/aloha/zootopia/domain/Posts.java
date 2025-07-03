package com.aloha.zootopia.domain;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 게시글 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Posts {

    private Integer postId;            // auto_increment
    private String id;                 // UUID
    private String category;          
    private String title;             
    private String content;           

    private Integer viewCount;        
    private Integer likeCount;        
    private Integer commentCount;     

    private Date createdAt;           
    private Date updatedAt;           

    private Long userId;           
    private Users user;               // 사용자 정보 포함 (JOIN된 users 테이블)

    private String thumbnailUrl;      // post_images 서브쿼리로 가져온 썸네일 URL

    private List<PostImage> images;   // 글에 첨부된 전체 이미지 목록 (선택적으로 Lazy 로딩)

     private List<Comment> comments;
}
