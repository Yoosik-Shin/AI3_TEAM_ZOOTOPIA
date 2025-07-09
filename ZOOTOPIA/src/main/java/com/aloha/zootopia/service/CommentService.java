package com.aloha.zootopia.service;

import java.util.List;

import com.aloha.zootopia.domain.Comment;

public interface CommentService {

    // 커뮤니티 댓글
    List<Comment> getCommentsByPostId(Integer postId);
    void addComment(Comment comment);
    void deleteComment(Integer commentId);
    void updateCommentContent(Comment comment);
    Comment findById(Integer commentId);
    List<Comment> getCommentsByPostIdAsTree(Integer postId);
}

