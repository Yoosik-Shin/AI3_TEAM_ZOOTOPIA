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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class HospitalService {
    @Autowired HospitalMapper hospitalMapper;
    @Autowired AnimalMapper animalMapper;
    @Autowired SpecialtyMapper specialtyMapper;
    @Autowired ReviewMapper reviewMapper;

    public List<Hospital> getHospitals(List<Integer> animalIds) {
        if(animalIds == null || animalIds.isEmpty()) return hospitalMapper.findAll();
        return hospitalMapper.findByAnimalIds(animalIds);
    }

    public PageInfo<Hospital> getHospitals(List<Integer> animalIds, int pageNum, int pageSize) {
                // PageHelper 설정 (SQL 실행 직전에 호출)
                PageHelper.startPage(pageNum, pageSize);
                
                try {
                    // 매퍼 메서드 호출
                    List<Hospital> hospitals = hospitalMapper.findByAnimalIds(animalIds);
                    
                    // PageInfo로 래핑
                    return new PageInfo<>(hospitals);
                } finally {
                    // 명시적 정리 (선택사항, 보통 자동으로 처리됨)
                    PageHelper.clearPage();
                }
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
