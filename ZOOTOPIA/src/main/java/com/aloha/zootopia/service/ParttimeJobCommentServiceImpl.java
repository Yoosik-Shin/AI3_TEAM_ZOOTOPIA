package com.aloha.zootopia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.zootopia.domain.ParttimeJobComment;
import com.aloha.zootopia.mapper.ParttimeJobCommentMapper;

@Service
public class ParttimeJobCommentServiceImpl implements ParttimeJobCommentService {
    
    @Autowired
    private ParttimeJobCommentMapper commentMapper;

    @Override
    public void registerComment(ParttimeJobComment comment) {
        commentMapper.insertComment(comment);
    }

    @Override
    public List<ParttimeJobComment> listComments(int jobId) {
        return commentMapper.selectCommentsByJobId(jobId);
    }

    @Override
    public void deleteComment(int commentId) {
        commentMapper.deleteComment(commentId);
    }  
}
