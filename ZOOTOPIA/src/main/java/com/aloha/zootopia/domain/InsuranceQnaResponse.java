package com.aloha.zootopia.domain;

import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class InsuranceQnaResponse {
    private int qnaId;
    private int productId;
    private String species;
    private String question;
    private String answer;
    private long userId;
    private String createdAt;  // 문자열로 포맷
    private boolean isWriter;
    private boolean isAdmin;

    // 기존 기본 변환 메서드
    public static InsuranceQnaResponse from(InsuranceQna qna) {
        return from(qna, -1, false); // 기본 사용자 정보 없음
    }


    // ✅ 로그인 사용자 정보를 포함하는 변환 메서드
    public static InsuranceQnaResponse from(InsuranceQna qna, long loginUserId, boolean isAdminUser) {
        InsuranceQnaResponse dto = new InsuranceQnaResponse();
        dto.setQnaId(qna.getQnaId());
        dto.setProductId(qna.getProductId());
        dto.setSpecies(qna.getSpecies());
        dto.setQuestion(qna.getQuestion());
        dto.setAnswer(qna.getAnswer());
        dto.setUserId(qna.getUserId());
        dto.setCreatedAt(
            qna.getCreatedAt() != null ?
            qna.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) : ""
        );
        dto.setWriter(qna.getUserId() == loginUserId);   // 작성자 여부
        dto.setAdmin(isAdminUser);                      // 관리자 여부
        return dto;
    }

}
