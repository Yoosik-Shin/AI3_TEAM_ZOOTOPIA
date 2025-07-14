package com.aloha.zootopia.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.aloha.zootopia.domain.CustomUser;

@ControllerAdvice
public class GlobalControllerAdvice {
    
     @ModelAttribute
    public void addProfileInfo(@AuthenticationPrincipal CustomUser user, Model model) {
        if (user != null) {
            model.addAttribute("profileImg", user.getUser().getProfileImg());
            model.addAttribute("nickname", user.getUser().getNickname());
        }
    }
}
