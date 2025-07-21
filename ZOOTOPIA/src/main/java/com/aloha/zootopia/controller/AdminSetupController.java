package com.aloha.zootopia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aloha.zootopia.domain.UserAuth;
import com.aloha.zootopia.domain.Users;
import com.aloha.zootopia.mapper.UserMapper;

@RestController
public class AdminSetupController {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/setup/admin")
    public String setupAdmin() {
        try {
            // 기존 admin@gmail.com 계정이 있는지 확인
            Users existingUser = null;
            try {
                existingUser = userMapper.select("admin@gmail.com");
            } catch (Exception e) {
                // 사용자가 없으면 null이 됨
            }
            
            if (existingUser != null) {
                return "Admin user already exists!";
            }
            
            // 새로운 관리자 계정 생성
            Users admin = new Users();
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin1234"));
            admin.setNickname("슈퍼어드민");
            admin.setPhone("010-1234-5678");
            
            // 사용자 등록
            int userResult = userMapper.join(admin);
            
            if (userResult > 0) {
                // 권한 등록
                UserAuth adminAuth = new UserAuth();
                adminAuth.setEmail("admin@gmail.com");
                adminAuth.setAuth("ROLE_ADMIN");
                userMapper.insertAuth(adminAuth);
                
                UserAuth superAdminAuth = new UserAuth();
                superAdminAuth.setEmail("admin@gmail.com");
                superAdminAuth.setAuth("ROLE_SUPER_ADMIN");
                userMapper.insertAuth(superAdminAuth);
                
                return "Admin user created successfully!<br>" +
                       "Email: admin@gmail.com<br>" +
                       "Password: admin1234<br>" +
                       "Roles: ROLE_ADMIN, ROLE_SUPER_ADMIN";
            } else {
                return "Failed to create admin user";
            }
            
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
