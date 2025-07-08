package com.aloha.zootopia.domain;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospReview {
    private Integer reviewId;
    private Integer hospitalId;
    private Integer rating;
    private String content;
    private Date createdAt;
    private Date
    private Integer userId;
    private String nickname;
    // getter/setter

}
