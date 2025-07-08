package com.aloha.zootopia.service;

import java.util.List;

import com.aloha.zootopia.domain.LostAnimalPost;
import com.aloha.zootopia.domain.Pagination;

public interface LostAnimalService {

    // 전체 목록 조회 (페이징 포함)
    List<LostAnimalPost> getAll(Pagination pagination);

    // 전체 게시글 수 조회
    long getTotalCount();

    // 단건 조회
    LostAnimalPost getById(int postId);

    // 게시글 등록
    boolean insert(LostAnimalPost post);

    // 게시글 수정
    boolean update(LostAnimalPost post);

    // 게시글 삭제
    boolean delete(int postId);

    // 조회수 증가
    void increaseViewCount(int postId);

    // 향후 확장을 위한 메서드 추가 가능:
    // void increaseCommentCount(int postId);
    // void decreaseCommentCount(int postId);
    // List<LostAnimalPost> searchByTag(String tag);
    // List<LostAnimalPost> getTopByViewsOrLikes();
}
