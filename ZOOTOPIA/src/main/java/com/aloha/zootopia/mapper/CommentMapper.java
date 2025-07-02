package com.aloha.zootopia.mapper;

import com.aloha.zootopia.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    /**
     * 특정 게시글의 댓글 목록 조회
     */
    List<Comment> findByPostId(Integer postId);

    /**
     * 댓글 등록
     */
    void insert(Comment comment);

    /**
     * 댓글 논리 삭제 (is_deleted = true)
     */
    void softDelete(Integer commentId);
}
