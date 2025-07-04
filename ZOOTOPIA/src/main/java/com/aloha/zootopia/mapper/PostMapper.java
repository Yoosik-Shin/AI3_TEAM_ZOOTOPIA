package com.aloha.zootopia.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aloha.zootopia.domain.Pagination;
import com.aloha.zootopia.domain.Posts;
import com.aloha.zootopia.domain.Tag;

@Mapper
public interface PostMapper {

    // 전체 목록
    List<Posts> list() throws Exception;

    // 카테고리 + 페이징
    List<Posts> page(Pagination pagination) throws Exception;

    // 카테고리별 인기글 TOP N
    List<Posts> selectTopNByLikeCount(int limit) throws Exception;

    // 전체 게시글 수
    long count(Pagination pagination) throws Exception;

    // 조회
    Posts selectById(String id) throws Exception;

    // 등록
    int insert(Posts post) throws Exception;

    // 수정
    int updateById(Posts post) throws Exception;

    // 삭제
    int deleteById(String id) throws Exception;

    int updateViewCount(int postId);

    int updateCommentCount(int postId);

    int minusCommentCount(int postId);

    List<Posts> selectTop10ByPopularity();

    List<Tag> selectTagsByPostIds(@Param("list") List<Integer> postIds);

    List<Posts> pageByCategory(@Param("category") String category);

    int updateThumbnail(Posts post) throws Exception;

}
