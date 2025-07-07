package com.aloha.zootopia.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aloha.zootopia.domain.Comment;
import com.aloha.zootopia.mapper.CommentMapper;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final PostService postService;
   

    @Override
    public List<Comment> getCommentsByPostId(Integer postId) {
        return commentMapper.findByPostId(postId);
    }

    @Override
    public void addComment(Comment comment) {
        commentMapper.insert(comment);
        postService.increaseCommentCount(comment.getPostId());
    }

    @Override
    public void deleteComment(Integer commentId) {
        commentMapper.softDelete(commentId);
    }
    
    @Override
    public void updateCommentContent(Comment comment) {
        commentMapper.updateContent(comment);
    }

    @Override
    public Comment findById(Integer commentId) {
        return commentMapper.findById(commentId);
    }

}
