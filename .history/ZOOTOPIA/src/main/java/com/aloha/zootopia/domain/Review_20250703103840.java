package com.aloha.zootopia.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    private Integer reviewId;
    private Integer hospitalId;
    private Integer rating;
    private String content;
    private String createdAt;
    private Integer userId;
    private String nickname;
    // getter/setter
}
