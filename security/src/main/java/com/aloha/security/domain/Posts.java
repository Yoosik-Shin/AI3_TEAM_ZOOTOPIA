package com.aloha.security.domain;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

// @AllArgsConstructor
// @NoArgsConstructor
// @Getter
// @Setter
// @ToString
@Data
@AllArgsConstructor
@Builder
public class Posts {

    private Long no;
    private String id;
    private String title;
    private Long userNo;
    private String content;
    private Date createdAt;
    private Date updatedAt;

    private Users user;

    public Posts() {
        this.id = UUID.randomUUID().toString();
    }

}
