package com.aloha.zootopia.domain;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    private Long userId;        // PK
    private String email;          // 이메일
    private String password;       // 비밀번호
    private String nickname;       // 닉네임
    private String intro;          // 자기소개
    private String phone;          // 전화번호
    private String profileImg;     // 프로필 이미지 경로
    private Date createdAt; // 가입일
    private int enabled;       // 계정 활성화 (1:활성, 0:비활성)
    
    private List<UserAuth> authList;
}
