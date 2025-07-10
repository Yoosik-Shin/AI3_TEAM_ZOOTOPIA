package com.aloha.zootopia.service.hospital;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.aloha.zootopia.domain.Animal;
import com.aloha.zootopia.domain.HospReview;
import com.aloha.zootopia.domain.Hospital;
import com.aloha.zootopia.domain.Specialty;
import com.aloha.zootopia.dto.HospReviewForm;
import com.aloha.zootopia.dto.HospitalForm;

public interface HospitalService {
    List<Hospital> getHospitals(List<Integer> animalIds);
    Hospital getHospital(Integer id);
    void createHospital(HospitalForm form, MultipartFile thumbnailImageFile) throws Exception;
    List<Animal> getAllAnimals();
    List<Specialty> getAllSpecialties();
    List<HospReview> getReviews(Integer hospitalId);
    void addReview(Integer hospitalId, HospReviewForm form, String nickname, Integer userId);
    List<Hospital> getHospitalList(List<Integer> animalIds, int pageNum, int pageSize);
    int getHospitalCount(List<Integer> animalIds);
    void updateReview(Integer reviewId, String content, Integer userId);
    void updateHospital(HospitalForm form, MultipartFile thumbnailImageFile) throws Exception;
    void deleteHospital(Integer id);
    void deleteReview(Integer reviewId, Integer userId);
}