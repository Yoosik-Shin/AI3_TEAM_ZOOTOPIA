package com.aloha.zootopia.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.zootopia.domain.ParttimeJobComment;

@Mapper
public interface ParttimeJobCommentMapper {
    void insertComment(ParttimeJobComment comment);
    List<ParttimeJobComment> selectCommentsByJobId(int jobId);
    void deleteComment(int commentId);
}
