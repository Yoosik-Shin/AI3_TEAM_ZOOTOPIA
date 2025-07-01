package com.aloha.security.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.security.domain.Pagination;
import com.aloha.security.domain.Posts;
import com.aloha.security.mapper.PostMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("PostService")
public class PostServiceImpl implements PostService {

    @Autowired private PostMapper postMapper;

    @Override
    public List<Posts> list() throws Exception {
        return postMapper.list();
    }

    @Override
    public Posts select(Integer no) throws Exception {
        return postMapper.select(no);
    }

    @Override
    public boolean insert(Posts post) throws Exception {
        return postMapper.insert(post) > 0;
    }
    
    @Override
    public boolean update(Posts post) throws Exception {
        return postMapper.update(post) > 0;
    }
    
    @Override
    public boolean delete(Integer no) throws Exception {
        return postMapper.delete(no) > 0;
    }

    @Override
    public List<Posts> page(Pagination pagination) throws Exception {
        // 데이터 수 조회
        long total = postMapper.count();
        pagination.setTotal(total);
        return postMapper.page(pagination);
    }

    /**
     * ⭐ PageHelper 페이징 목록
     */
    @Override
    public PageInfo<Posts> page(int page, int size) throws Exception {
        // PageHelper.startPage(현재 번호, 페이지당 데이터 수)
        PageHelper.startPage(page, size);
        List<Posts> list = postMapper.list();

        // ⭐ PageInfo<DTO>( 리스트, 노출 페이지 수 )
        PageInfo<Posts> pageInfo = new PageInfo<>(list, 10);
        return pageInfo;
    }

    @Override
    public Posts selectById(String id) throws Exception {
        Posts post = postMapper.selectById(id);
        return post;
    }

    @Override
    public boolean updateById(Posts post) throws Exception {
        return postMapper.updateById(post) > 0;
    }
    
    @Override
    public boolean deleteById(String id) throws Exception {
        return postMapper.deleteById(id) > 0;
    }

    /**
     * @param id        : 게시글 id
     * @param userNo    : 회원 no (PK)
     * 게시글 id로 작성자 userNo 를 조회하여,
     * 인증된 사용자 no 와 일치하는지 확인
     */
    @Override
    public boolean isOwner(String id, Long userNo) throws Exception {
        log.info("isOwner - id : {}", id);
        log.info("isOwner - userNo : {}", userNo);

        Posts post = selectById(id);
        Long postUserNo = post.getUserNo();
        if( userNo != null && userNo == postUserNo ) {
            return true;
        }
        return false;
    }
    
}
