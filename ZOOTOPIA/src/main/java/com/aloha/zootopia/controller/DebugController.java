package com.aloha.zootopia.controller;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/debug")
public class DebugController {
    
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Debug Controller is working!";
    }
    
    @GetMapping("/simple-page")
    public String simplePage(Model model) {
        model.addAttribute("message", "간단한 페이지 테스트");
        return "debug/simple";
    }
    
    @GetMapping("/product-test/{no}")
    public String productTest(@PathVariable int no, Model model) {
        System.out.println("Debug: Product test called with no = " + no);
        
        model.addAttribute("productNo", no);
        model.addAttribute("productName", "테스트상품" + no);
        model.addAttribute("productPrice", no * 1000);
        
        return "debug/product_test";
    }
    
    /**
     * 현재 로그인한 사용자의 권한 정보를 확인하는 디버깅 엔드포인트
     * URL: /debug/auth
     */
    @GetMapping("/auth")
    @ResponseBody
    public String getCurrentUserAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth == null) {
            return "인증 정보가 없습니다.";
        }
        
        String username = auth.getName();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        
        String authList = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
        
        StringBuilder result = new StringBuilder();
        result.append("=== 현재 사용자 권한 정보 ===\n");
        result.append("사용자명: ").append(username).append("\n");
        result.append("권한 목록: ").append(authList).append("\n");
        result.append("총 권한 수: ").append(authorities.size()).append("\n");
        result.append("\n=== 권한 상세 ===\n");
        
        for (GrantedAuthority authority : authorities) {
            result.append("- ").append(authority.getAuthority()).append("\n");
        }
        
        result.append("\n=== 권한 확인 ===\n");
        result.append("ROLE_USER 권한: ").append(authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))).append("\n");
        result.append("ROLE_ADMIN 권한: ").append(authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))).append("\n");
        
        return result.toString();
    }
}