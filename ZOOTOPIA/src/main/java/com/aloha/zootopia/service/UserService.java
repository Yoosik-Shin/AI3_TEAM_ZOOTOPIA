package com.aloha.zootopia.service;

import com.aloha.zootopia.domain.UserAuth;
import com.aloha.zootopia.domain.Users;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    // 회원 가입
    public int join(Users user) throws Exception;
    
    // 회원 권한 등록
    public int insertAuth(UserAuth userAuth) throws Exception;

    // 🔐 로그인
    public boolean login(Users user, HttpServletRequest request);

    // 회원 조회
    public Users select(String username) throws Exception;

    // 👮‍♀️ 관리자 확인
    public boolean isAdmin() throws Exception;
    
}
