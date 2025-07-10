package com.aloha.zootopia.service.hospital;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired HospitalMapper hospitalMapper;
    @Autowired AnimalMapper animalMapper;
    @Autowired SpecialtyMapper specialtyMapper;
    @Autowired HospReviewMapper reviewMapper;

    @Value("${file.upload.path}")
    private String uploadPath;

    // 이미지 저장 헬퍼 메서드
    private String saveImage(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        String savedFilename = uuid + extension;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        File targetFile = new File(uploadPath, savedFilename);
        file.transferTo(targetFile);
        return "/upload/" + savedFilename; // Return a relative path for web access
    }

    @Override
    public List<Hospital> getHospitals(List<Integer> animalIds) {
        if(animalIds == null || animalIds.isEmpty()) return hospitalMapper.findAll();
        return hospitalMapper.findByAnimalIds(animalIds);
    }

    @Override
    public Hospital getHospital(Integer id) {
        log.info("############################################################");
        log.info("HospitalServiceImpl - getHospital() 진입");
        Hospital hospital = hospitalMapper.findById(id);
        if (hospital != null) {
            log.info("Mapper.findById() 호출 후 병원 기본 정보 조회 완료. Hospital ID: {}", id);
            List<HospReview> reviews = hospitalMapper.getReviewsByHospitalId(id);
            log.info("Mapper.getReviewsByHospitalId() 호출 후 리뷰 개수: {}", reviews != null ? reviews.size() : 0);
            List<Animal> animals = hospitalMapper.getAnimalsByHospitalId(id);
            log.info("Mapper.getAnimalsByHospitalId() 호출 후 동물 개수: {}", animals != null ? animals.size() : 0);
            List<Specialty> specialties = hospitalMapper.getSpecialtiesByHospitalId(id);
            log.info("Mapper.getSpecialtiesByHospitalId() 호출 후 진료과목 개수: {}", specialties != null ? specialties.size() : 0);

            hospital.setReviews(reviews);
            hospital.setAnimals(animals);
            hospital.setSpecialties(specialties);
            log.info("Hospital 객체에 리뷰, 동물, 진료과목 설정 완료. 최종 리뷰 개수: {}", hospital.getReviews() != null ? hospital.getReviews().size() : 0);
        } else {
            log.warn("HospitalServiceImpl - getHospital(): Hospital ID {} 에 해당하는 병원을 찾을 수 없습니다.", id);
        }
        log.info("############################################################");
        return hospital;
    }

    @Override
    public void createHospital(HospitalForm form, MultipartFile thumbnailImageFile) throws Exception {
        String thumbnailImageUrl = saveImage(thumbnailImageFile);
        form.setThumbnailImageUrl(thumbnailImageUrl);

        Hospital hospital = new Hospital();
        hospital.setName(form.getName());
        hospital.setAddress(form.getAddress());
        hospital.setHomepage(form.getHomepage());
        hospital.setPhone(form.getPhone());
        hospital.setEmail(form.getEmail());
        hospital.setThumbnailImageUrl(form.getThumbnailImageUrl());
        hospital.setHospIntroduce(form.getHospIntroduce());
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
    public List<HospReview> getReviews(Integer hospitalId) {
        List<HospReview> reviews = reviewMapper.findByHospitalId(hospitalId);
        System.out.println("DEBUG: getReviews for hospitalId " + hospitalId + " returned " + (reviews != null ? reviews.size() : "null") + " reviews.");
        return reviews;
    }

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
    public void deleteReview(Integer reviewId, Integer userId) {
        HospReview review = reviewMapper.findById(reviewId);
        if (review == null) {
            // 리뷰가 이미 삭제되었거나 존재하지 않는 경우
            return; 
        }
        if (!review.getUserId().equals(userId)) {
            throw new SecurityException("삭제할 권한이 없습니다.");
        }
        reviewMapper.deleteReview(reviewId, userId);
    }

    @Override
    public void updateHospital(HospitalForm form, MultipartFile thumbnailImageFile) throws Exception {
        // 기존 병원 정보 조회 (기존 이미지 URL을 얻기 위함)
        Hospital existingHospital = hospitalMapper.findById(form.getHospitalId());
        String oldThumbnailImageUrl = existingHospital != null ? existingHospital.getThumbnailImageUrl() : null;

        String newThumbnailImageUrl = saveImage(thumbnailImageFile);

        // 새 이미지가 업로드되었으면 기존 이미지 삭제
        if (newThumbnailImageUrl != null && oldThumbnailImageUrl != null) {
            File oldImageFile = new File(uploadPath, oldThumbnailImageUrl.replace("/upload/", ""));
            if (oldImageFile.exists()) {
                oldImageFile.delete();
            }
        }
        // 새 이미지가 없으면 기존 이미지 URL 유지
        if (newThumbnailImageUrl == null) {
            form.setThumbnailImageUrl(oldThumbnailImageUrl);
        } else {
            form.setThumbnailImageUrl(newThumbnailImageUrl);
        }

        Hospital hospital = new Hospital();
        hospital.setHospitalId(form.getHospitalId());
        hospital.setName(form.getName());
        hospital.setAddress(form.getAddress());
        hospital.setHomepage(form.getHomepage());
        hospital.setPhone(form.getPhone());
        hospital.setEmail(form.getEmail());
        hospital.setThumbnailImageUrl(form.getThumbnailImageUrl());
        hospital.setHospIntroduce(form.getHospIntroduce()); // 추가

        hospitalMapper.updateHospital(hospital);

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
        // 병원 정보 조회하여 이미지 파일 경로 가져오기
        Hospital hospital = hospitalMapper.findById(id);
        if (hospital != null && hospital.getThumbnailImageUrl() != null) {
            File imageFile = new File(uploadPath, hospital.getThumbnailImageUrl().replace("/upload/", ""));
            if (imageFile.exists()) {
                imageFile.delete();
            }
        }
        reviewMapper.deleteReviewsByHospitalId(id);
        hospitalMapper.deleteHospitalAnimals(id);
        hospitalMapper.deleteHospitalSpecialties(id);
        hospitalMapper.deleteHospital(id);
    }


}