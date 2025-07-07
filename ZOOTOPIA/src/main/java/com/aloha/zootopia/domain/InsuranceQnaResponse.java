package com.aloha.zootopia.domain;

import lombok.Data;

@Data
public class InsuranceQnaResponse {
    private int qnaId;
    private int productId;
    private String species;
    private String question;
    private String answer;
    private int userId;
    private String createdAt;  // 문자열로 포맷
}
