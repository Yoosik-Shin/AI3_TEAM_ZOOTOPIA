package com.aloha.zootopia.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aloha.zootopia.domain.HospReview;

@Mapper
public interface HospReviewMapper {
    List<HospReview> findByHospitalId(@Param("hospitalId") Integer hospitalId);
    void insertReview(HospReview review);
    Double findAvgRating(@Param("hospitalId") Integer hospitalId);
    int findReviewCount(@Param("hospitalId") Integer hospitalId);
    int deleteReview(@Param("reviewId") Integer reviewId, @Param("userId") Integer userId);
    void updateReview(@Param("reviewId") Integer reviewId, @Param("content") String content, @Param("userId") Integer userId);
    HospReview findById(@Param("reviewId") Integer reviewId);
}
