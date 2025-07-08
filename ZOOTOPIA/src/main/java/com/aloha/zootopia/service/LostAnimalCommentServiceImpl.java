package com.aloha.zootopia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.zootopia.domain.Comment;
import com.aloha.zootopia.mapper.CommentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LostAnimalCommentServiceImpl implements LostAnimalCommentService{
    
     @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> getCommentsByPostId(Integer postId) {
        return commentMapper.findLostByPostId(postId);
    }

    @Override
    public void addComment(Comment comment) {
        commentMapper.insertLost(comment);
    }

    @Override
    public void deleteComment(Integer commentId) {
        commentMapper.softDeleteLost(commentId);
    }

    @Override
    public void updateCommentContent(Comment comment) {
        commentMapper.updateLostContent(comment);
    }

    @Override
    public Comment findById(Integer commentId) {
        return commentMapper.findLostById(commentId);
    }
}


