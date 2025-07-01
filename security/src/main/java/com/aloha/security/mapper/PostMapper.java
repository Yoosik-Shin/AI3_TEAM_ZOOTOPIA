package com.aloha.security.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.security.domain.Pagination;
import com.aloha.security.domain.Posts;

@Mapper
public interface PostMapper {

    // 목록
    public List<Posts> list() throws Exception;
    // 페이징 목록
    public List<Posts> page(Pagination pagination) throws Exception;
    // 데이터 수
    public long count() throws Exception;
    // 조회
    public Posts select(Integer no) throws Exception;
    // 조회 - id
    public Posts selectById(String id) throws Exception;
    // 등록
    public int insert(Posts post) throws Exception;
    // 수정
    public int update(Posts post) throws Exception;
    // 수정 - id
    public int updateById(Posts post) throws Exception;
    // 삭제
    public int delete(Integer no) throws Exception;
    // 삭제 - id
    public int deleteById(String id) throws Exception;
    
}
