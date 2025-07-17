package com.aloha.zootopia.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        
        log.info("소셜 로그아웃을 진행합니다...");

        String naverLogoutUrl = "https://nid.naver.com/nidlogin.logout.html?client_id=" + naverClientId + "&service_url=http://localhost:8080/";

        // 네이버 로그아웃 URL로 리디렉션
        response.sendRedirect(naverLogoutUrl);
    }
}