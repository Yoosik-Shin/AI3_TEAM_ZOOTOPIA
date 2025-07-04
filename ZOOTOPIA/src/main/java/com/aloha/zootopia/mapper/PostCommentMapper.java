package com.aloha.zootopia.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.aloha.zootopia.domain.PostComment;

@Mapper
public interface PostCommentMapper {

    // 게시글의 댓글 목록
    List<PostComment> findByPostId(int postId) throws Exception;

    // 댓글 1개 조회
    PostComment selectById(int commentId) throws Exception;

    // 등록
    int insert(PostComment comment) throws Exception;

    // 수정
    int update(PostComment comment) throws Exception;

    // 삭제 (Soft Delete 처리 시 is_deleted=true)
    int delete(int commentId) throws Exception;
}
