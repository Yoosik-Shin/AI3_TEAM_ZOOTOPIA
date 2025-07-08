package com.aloha.zootopia.service;

import java.util.List;

import com.aloha.zootopia.domain.Animal;
import com.aloha.zootopia.domain.Hospital;
import com.aloha.zootopia.domain.HospReview;
import com.aloha.zootopia.domain.Specialty;
import com.aloha.zootopia.dto.HospitalForm;
import com.aloha.zootopia.dto.HospReviewForm;
// import com.github.pagehelper.PageInfo;

public interface HospitalService {
    List<Hospital> getHospitals(List<Integer> animalIds);
    Hospital getHospital(Integer id);
    void createHospital(HospitalForm form);
    List<Animal> getAllAnimals();
    List<Specialty> getAllSpecialties();
    List<HospReview> getReviews(Integer hospitalId);
    void addReview(Integer hospitalId, HospReviewForm form, String nickname, Integer userId);
    // List<Hospital> getHospitalList(int pageNum, int pageSize, List<Long> animalIds, PageInfo pageInfo);
    // PageInfo<Hospital> getHospitalList(List<Integer> animal, int pageNum, int pageSize);
    List<Hospital> getHospitalList(List<Integer> animalIds, int pageNum, int pageSize);
    int getHospitalCount(List<Integer> animalIds);
    void updateReview(Integer reviewId, String content, Integer userId);
    void updateHospital(HospitalForm form);
    void deleteHospital(Integer id);
}