package com.aloha.zootopia.service;

import java.util.List;

import com.aloha.zootopia.domain.Pagination;
import com.aloha.zootopia.domain.Posts;
import com.github.pagehelper.PageInfo;

public interface PostService {

    // 게시글 전체 목록
    List<Posts> list() throws Exception;

    // 페이징 목록
    List<Posts> page(Pagination pagination) throws Exception;

    // PageHelper 기반 페이징
    PageInfo<Posts> page(int page, int size) throws Exception;

    // 인기글 Top N
    List<Posts> getTopN(int limit) throws Exception;

    // 단건 조회
    Posts selectById(String id) throws Exception;

    // 등록 
    boolean insert(Posts post) throws Exception;

    // 수정
    boolean updateById(Posts post) throws Exception;

    // 삭제
    boolean deleteById(String id) throws Exception;

    // 소유자 검증
    boolean isOwner(String id, Long userId) throws Exception;

    void increaseViewCount(Integer postId);

    
    List<Posts> getTop10PopularPosts();
    


}
