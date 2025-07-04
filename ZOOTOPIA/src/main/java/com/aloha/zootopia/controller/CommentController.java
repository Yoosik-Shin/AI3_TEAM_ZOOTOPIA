package com.aloha.zootopia.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aloha.zootopia.domain.Comment;
import com.aloha.zootopia.domain.CustomUser;
import com.aloha.zootopia.service.CommentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    

    @PostMapping("/add")
    public String addComment(@ModelAttribute Comment comment, @AuthenticationPrincipal CustomUser user) throws Exception {
  

        comment.setUserId(user.getUser().getUserId());
        commentService.addComment(comment);
        

        return "redirect:/posts/read/" + comment.getPostId(); 
    }

    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable("id") Integer commentId,
                                 @RequestParam("postId") Integer postId) {
        commentService.deleteComment(commentId);
        return "redirect:/posts/read/" + postId;
    }
}
