package com.aloha.zootopia.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.aloha.zootopia.domain.PostImage;

@Mapper
public interface PostImageMapper {

    // 게시글 이미지 목록
    List<PostImage> findByPostId(int postId) throws Exception;

    // 등록
    int insert(PostImage image) throws Exception;

    // 삭제 (게시글의 전체 이미지)
    int deleteByPostId(int postId) throws Exception;

    // 대표 이미지 1장 가져오기
    PostImage selectThumbnailByPostId(int postId) throws Exception;
}
