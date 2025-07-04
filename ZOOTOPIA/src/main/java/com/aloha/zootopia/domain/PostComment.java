package com.aloha.zootopia.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 게시글 댓글 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostComment {

    private Integer commentId;     // 댓글 ID (PK)
    private String content;        // 댓글 내용
    private Date createdAt;        // 작성일
    private Date updatedAt;        // 수정일
    private Boolean isDeleted;     // 삭제 여부
    private Integer userId;        // 작성자 ID (FK)
    private Integer postId;        // 게시글 ID (FK)


}
