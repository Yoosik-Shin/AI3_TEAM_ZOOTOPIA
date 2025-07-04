package com.aloha.zootopia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aloha.zootopia.domain.ParttimeJobComment;
import com.aloha.zootopia.service.ParttimeJobCommentService;

@Controller
@RequestMapping("/parttime/job/comment")
public class ParttimeJobCommentController {

    @Autowired
    private ParttimeJobCommentService commentService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('USER')")
    public String registerComment(ParttimeJobComment comment, @AuthenticationPrincipal User user) {
        comment.setUserId(Integer.parseInt(user.getUsername()));
        commentService.registerComment(comment);
        return "redirect:/parttime/detail/" + comment.getJobId();
    }

    @PostMapping("/delete/{commentId}")
    @PreAuthorize("hasRole('USER')")
    public String deleteComment(@PathVariable int commentId, @RequestParam int jobId, @AuthenticationPrincipal User user) {
        // 댓글 작성자 본인인지 체크 (추후 Service에 검증 추가 가능)
        commentService.deleteComment(commentId);
        return "redirect:/parttime/detail/" + jobId;
    }
}
