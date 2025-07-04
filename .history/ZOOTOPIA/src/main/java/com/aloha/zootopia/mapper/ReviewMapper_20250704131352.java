package com.aloha.zootopia.mapper;

<<<<<<< HEADimport java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aloha.zootopia.domain.Review;

@Mapper
public interface ReviewMapper {
    List<Review> findByHospitalId(@Param("hospitalId") Integer hospitalId);
    void insertReview(Review review);
    Double findAvgRating(@Param("hospitalId") Integer hospitalId);

}
