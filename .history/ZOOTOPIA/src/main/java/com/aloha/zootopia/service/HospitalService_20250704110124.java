package com.aloha.zootopia.service;

import java.util.List;

import com.aloha.zootopia.domain.Animal;
import com.aloha.zootopia.domain.Hospital;
import com.aloha.zootopia.domain.Review;
import com.aloha.zootopia.domain.Specialty;
import com.aloha.zootopia.dto.HospitalForm;
import com.aloha.zootopia.dto.ReviewForm;
import com.github.pagehelper.PageInfo;

public interface HospitalService {
    List<Hospital> getHospitals(List<Integer> animalIds);
    Hospital getHospital(Integer id);
    void createHospital(HospitalForm form);
    List<Animal> getAllAnimals();
    List<Specialty> getAllSpecialties();
    List<Review> getReviews(Integer hospitalId);
    void addReview(Integer hospitalId, ReviewForm form, String nickname, Integer userId);
    List<Hospital> getHospitalList(int pageNum, int pageSize, List<Long> animalIds, PageInfo pageInfo);
    int getHospitalCount(List<Long> animalIds);
}