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

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired HospitalMapper hospitalMapper;
    @Autowired AnimalMapper animalMapper;
    @Autowired SpecialtyMapper specialtyMapper;
    @Autowired ReviewMapper reviewMapper;

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
    public List<Review> getReviews(Integer hospitalId) { return reviewMapper.findByHospitalId(hospitalId); }

    @Override
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