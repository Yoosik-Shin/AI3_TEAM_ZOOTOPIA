package com.aloha.zootopia.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ParttimeJobApplicant {
    private int applicantId;
    private int jobId;            // 지원한 알바 ID
    private int userId;           // 지원자
    private float rating;         // 평점
    private int reviewCount;
    private String introduction;  // 자기소개
    private LocalDateTime createdAt;
}
