package com.aloha.zootopia.mapper;

import com.aloha.zootopia.domain.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {
    
    /**
     * 특정 상품의 리뷰 목록 조회
     * @param productId 상품 ID
     * @return 리뷰 목록
     */
    List<Review> findByProductId(@Param("productId") Long productId);
    
    /**
     * 리뷰 등록
     * @param review 리뷰 정보
     * @return 등록된 행 수
     */
    int insert(Review review);
    
    /**
     * 리뷰 수정
     * @param review 리뷰 정보
     * @return 수정된 행 수
     */
    int update(Review review);
    
    /**
     * 리뷰 삭제
     * @param id 리뷰 ID
     * @return 삭제된 행 수
     */
    int delete(@Param("id") Long id);
    
    /**
     * 특정 상품의 평균 평점 조회
     * @param productId 상품 ID
     * @return 평균 평점
     */
    Double getAverageRating(@Param("productId") Long productId);
    
    /**
     * 특정 상품의 리뷰 개수 조회
     * @param productId 상품 ID
     * @return 리뷰 개수
     */
    int getReviewCount(@Param("productId") Long productId);
    
    /**
     * 사용자별 특정 상품 리뷰 조회 (중복 리뷰 방지용)
     * @param productId 상품 ID
     * @param userId 사용자 ID
     * @return 리뷰 정보
     */
    Review findByProductIdAndUserId(@Param("productId") Long productId, @Param("userId") Long userId);
}
