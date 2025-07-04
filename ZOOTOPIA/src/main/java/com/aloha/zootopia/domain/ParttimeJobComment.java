package com.aloha.zootopia.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ParttimeJobComment {
    private int commentId;
    private int jobId;          // 소속된 알바글
    private int userId;         // 작성자
    private String content;
    private LocalDateTime createdAt;
}
