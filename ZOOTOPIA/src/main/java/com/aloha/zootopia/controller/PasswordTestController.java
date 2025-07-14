package com.aloha.zootopia.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordTestController {
    
    @GetMapping("/test/password")
    public String testPassword(@RequestParam String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoded = encoder.encode(password);
        
        // 기존 해시와 비교
        String existingHash = "$2a$10$2XMPFzReUtpL32VoJznvmuD0n1eV5BNSczGb3oFGdDtd.6cqW5R5O";
        boolean matches = encoder.matches(password, existingHash);
        
        return String.format(
            "Password: %s<br>" +
            "New Hash: %s<br>" +
            "Existing Hash: %s<br>" +
            "Matches: %s", 
            password, encoded, existingHash, matches
        );
    }
}
