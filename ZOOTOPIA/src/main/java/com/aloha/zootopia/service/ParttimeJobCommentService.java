package com.aloha.zootopia.service;

import java.util.List;

import com.aloha.zootopia.domain.ParttimeJobComment;

public interface ParttimeJobCommentService {
    void registerComment(ParttimeJobComment comment);
    List<ParttimeJobComment> listComments(int jobId);
    void deleteComment(int commentId);
}
