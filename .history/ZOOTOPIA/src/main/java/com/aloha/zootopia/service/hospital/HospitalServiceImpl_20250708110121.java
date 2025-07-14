package com.aloha.zootopia.service.hospital;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.zootopia.domain.Animal;
import com.aloha.zootopia.domain.Hospital;
import com.aloha.zootopia.domain.HospReview;
import com.aloha.zootopia.domain.Specialty;
import com.aloha.zootopia.dto.HospitalForm;
import com.aloha.zootopia.dto.PageInfo;
import com.aloha.zootopia.dto.HospReviewForm;
import com.aloha.zootopia.mapper.AnimalMapper;
import com.aloha.zootopia.mapper.HospitalMapper;
import com.aloha.zootopia.mapper.HospReviewMapper;
import com.aloha.zootopia.mapper.SpecialtyMapper;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired HospitalMapper hospitalMapper;
    @Autowired AnimalMapper animalMapper;
    @Autowired SpecialtyMapper specialtyMapper;
    @Autowired HospReviewMapper reviewMapper;

    @Override
    public List<Hospital> getHospitals(List<Integer> animalIds) {
        if(animalIds == null || animalIds.isEmpty()) return hospitalMapper.findAll();
        return hospitalMapper.findByAnimalIds(animalIds);
    }

    @Override
    public Hospital getHospital(Integer id) { return hospitalMapper.findById(id); }

    @Override
    public void createHospital(HospitalForm form) {
        Hospital hospital = new Hospital();
        hospital.setName(form.getName());
        hospital.setAddress(form.getAddress());
        hospital.setHomepage(form.getHomepage());
        hospital.setPhone(form.getPhone());
        hospital.setEmail(form.getEmail());
        hospital.setThumbnailImageUrl(form.getThumbnailImageUrl()); // 추가될 코드
        hospitalMapper.insertHospital(hospital);
        for(Integer animalId : form.getAnimalIds()) {
            hospitalMapper.insertHospitalAnimal(hospital.getHospitalId(), animalId);
        }
        for(Integer specialtyId : form.getSpecialtyIds()) {
            hospitalMapper.insertHospitalSpecialty(hospital.getHospitalId(), specialtyId);
        }
    }

    @Override
    public List<Animal> getAllAnimals() { return animalMapper.findAll(); }

    @Override
    public List<Specialty> getAllSpecialties() { return specialtyMapper.findAll(); }

    @Override
    public List<HospReview> getReviews(Integer hospitalId) { return reviewMapper.findByHospitalId(hospitalId); }

    @Override
    public void addReview(Integer hospitalId, HospReviewForm form, String nickname, Integer userId) {
        HospReview review = new HospReview();
        review.setHospitalId(hospitalId);
        review.setRating(form.getRating());
        review.setContent(form.getContent());
        review.setNickname(nickname);
        review.setUserId(userId);
        reviewMapper.insertReview(review);
    }


public HospitalServiceImpl(HospitalMapper hospitalMapper) {
        this.hospitalMapper = hospitalMapper;
    }

    @Override
    public List<Hospital> getHospitalList(List<Integer> animalIds, int pageNum, int pageSize) {

        int offset = (pageNum - 1) * pageSize;
        return hospitalMapper.selectHospitals(offset, pageSize, animalIds);
    }

    @Override
    public int getHospitalCount(List<Integer> animalIds) {
        return hospitalMapper.countHospitals(animalIds);
    }

    @Override
    public void updateReview(Integer reviewId, String content, Integer userId) {
        HospReview review = reviewMapper.findById(reviewId);
        if (review == null) {
            throw new IllegalArgumentException("Review not found.");
        }
        if (!review.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to update this review.");
        }
        reviewMapper.updateReview(reviewId, content, userId);
    }

    @Override
    public void updateHospital(HospitalForm form) {
        Hospital hospital = new Hospital();
        hospital.setHospitalId(form.getHospitalId()); // ID 설정
        hospital.setName(form.getName());
        hospital.setAddress(form.getAddress());
        hospital.setHomepage(form.getHomepage());
        hospital.setPhone(form.getPhone());
        hospital.setEmail(form.getEmail());
        hospital.setThumbnailImageUrl(form.getThumbnailImageUrl());

        hospitalMapper.updateHospital(hospital); // 병원 기본 정보 업데이트

        // 기존 동물 및 진료 과목 연결 삭제 후 새로 추가
        hospitalMapper.deleteHospitalAnimals(hospital.getHospitalId());
        hospitalMapper.deleteHospitalSpecialties(hospital.getHospitalId());

        if (form.getAnimalIds() != null) {
            for(Integer animalId : form.getAnimalIds()) {
                hospitalMapper.insertHospitalAnimal(hospital.getHospitalId(), animalId);
            }
        }
        if (form.getSpecialtyIds() != null) {
            for(Integer specialtyId : form.getSpecialtyIds()) {
                hospitalMapper.insertHospitalSpecialty(hospital.getHospitalId(), specialtyId);
            }
        }
    }

    @Override
    public void deleteHospital(Integer id) {
        // 관련 리뷰 삭제
        reviewMapper.deleteReviewsByHospitalId(id); // HospReviewMapper에 deleteReviewsByHospitalId 메서드 필요
        // 관련 동물 연결 삭제
        hospitalMapper.deleteHospitalAnimals(id);
        // 관련 진료 과목 연결 삭제
        hospitalMapper.deleteHospitalSpecialties(id);
        // 병원 정보 삭제
        hospitalMapper.deleteHospital(id); // HospitalMapper에 deleteHospital 메서드 필요
    }


}