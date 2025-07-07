package com.aloha.zootopia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.zootopia.domain.Animal;
import com.aloha.zootopia.domain.Hospital;
import com.aloha.zootopia.domain.Review;
import com.aloha.zootopia.domain.Specialty;
import com.aloha.zootopia.dto.HospitalForm;
import com.aloha.zootopia.dto.ReviewForm;
import com.aloha.zootopia.mapper.AnimalMapper;
import com.aloha.zootopia.mapper.HospitalMapper;
import com.aloha.zootopia.mapper.ReviewMapper;
import com.aloha.zootopia.mapper.SpecialtyMapper;
import com.github.pagehelper.PageInfo;

@Service
public interface HospitalService {

    /**
        * 병원 목록을 동물 종류에 따라 페이징 처리하여 반환합니다.
        *
        * @param animal 동물 종류 ID 리스트
        * @param pageNum 페이지 번호
        * @param pageSize 페이지 크기
        * @return 병원 목록과 페이징 정보
        */

    PageInfo<Hospital> getHospitalsWithPaging(List<Integer> animal, int pageNum, int pageSize);
    /**
        * 주어진 동물 ID 리스트에 해당하는 병원 목록을 반환합니다.
        *
        * @param animalIds 동물 ID 리스트
        * @return 병원 목록
        */
    List<Hospital> getHospitals(List<Integer> animalIds);
    /**
        * 병원 ID로 병원을 조회합니다.
        *
        * @param id 병원 ID
        * @return 병원 정보
        */
} HospitalService {
    @Autowired HospitalMapper hospitalMapper;
    @Autowired AnimalMapper animalMapper;
    @Autowired SpecialtyMapper specialtyMapper;
    @Autowired ReviewMapper reviewMapper;

    PageInfo<Hospital> getHospitalsWithPaging(List<Integer> animal, int pageNum, int pageSize);

    public List<Hospital> getHospitals(List<Integer> animalIds) {
        if(animalIds == null || animalIds.isEmpty()) return hospitalMapper.findAll();
        return hospitalMapper.findByAnimalIds(animalIds);
    }





    
    public Hospital getHospital(Integer id) { return hospitalMapper.findById(id); }
    public void createHospital(HospitalForm form) {
        Hospital hospital = new Hospital();
        hospital.setName(form.getName());
        hospital.setAddress(form.getAddress());
        hospital.setHomepage(form.getHomepage());
        hospital.setPhone(form.getPhone());
        hospital.setEmail(form.getEmail());
        hospitalMapper.insertHospital(hospital);
        for(Integer animalId : form.getAnimalIds()) {
            hospitalMapper.insertHospitalAnimal(hospital.getHospitalId(), animalId);
        }
        for(Integer specialtyId : form.getSpecialtyIds()) {
            hospitalMapper.insertHospitalSpecialty(hospital.getHospitalId(), specialtyId);
        }
    }
    public List<Animal> getAllAnimals() { return animalMapper.findAll(); }

    public List<Specialty> getAllSpecialties() { return specialtyMapper.findAll(); }

    public List<Review> getReviews(Integer hospitalId) { return reviewMapper.findByHospitalId(hospitalId); }

    public void addReview(Integer hospitalId, ReviewForm form, String nickname, Integer userId) {
        Review review = new Review();
        review.setHospitalId(hospitalId);
        review.setRating(form.getRating());
        review.setContent(form.getContent());
        review.setNickname(nickname);
        review.setUserId(userId);
        reviewMapper.insertReview(review);
    }

    




}
