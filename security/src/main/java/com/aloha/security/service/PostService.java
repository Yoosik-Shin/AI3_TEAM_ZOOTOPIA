package com.aloha.security.service;

import java.util.List;

import com.aloha.security.domain.Pagination;
import com.aloha.security.domain.Posts;
import com.github.pagehelper.PageInfo;

public interface PostService {

    // 목록
    public List<Posts> list() throws Exception;
    // 페이징 목록
    public List<Posts> page(Pagination pagination) throws Exception;
    // ⭐ pagehelper 를 이용한 페이징 목록
    public PageInfo<Posts> page(int page, int size) throws Exception;
    // 조회
    public Posts select(Integer no) throws Exception;
    // 조회 - id
    public Posts selectById(String id) throws Exception;
    // 등록
    public boolean insert(Posts post) throws Exception;
    // 수정
    public boolean update(Posts post) throws Exception;
    // 수정 - id
    public boolean updateById(Posts post) throws Exception;
    // 삭제
    public boolean delete(Integer no) throws Exception;
    // 삭제 - id
    public boolean deleteById(String id) throws Exception;

    // 소유자 확인
    public boolean isOwner(String id, Long userNo) throws Exception;
    
}
