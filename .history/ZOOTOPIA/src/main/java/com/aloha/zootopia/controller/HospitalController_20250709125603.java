package com.aloha.zootopia.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // Import BindingResult
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aloha.zootopia.domain.Hospital;
import com.aloha.zootopia.dto.HospReviewForm;
import com.aloha.zootopia.dto.HospitalForm;
import com.aloha.zootopia.dto.PageInfo;
import com.aloha.zootopia.service.hospital.HospitalService;

import jakarta.validation.Valid; // Import @Valid

@Controller
@RequestMapping("/hospitals")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    

    // 병원 목록 페이지
    @GetMapping
    public String list(
        @RequestParam(required = false) List<Integer> animal,
        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
        Model model) {

        int pageSize = 6;
        int total = hospitalService.getHospitalCount(animal);
        List<Hospital> hospitalList = hospitalService.getHospitalList(animal, pageNum, pageSize);

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(total);
        int pages = (int) Math.ceil((double) total / pageSize);
        pageInfo.setPages(pages);

        // 네비게이션 계산 (5개씩)
        int navSize = 5;
        int startPage = ((pageNum - 1) / navSize) * navSize + 1;
        int endPage = Math.min(startPage + navSize - 1, pages);
        pageInfo.setStartPage(startPage);
        pageInfo.setEndPage(endPage);
        pageInfo.setHasPreviousPage(pageNum > 1);
        pageInfo.setHasNextPage(pageNum < pages);
        pageInfo.setHasFirstPage(pages > 1);
        pageInfo.setHasLastPage(endPage < pages);

        model.addAttribute("hospitalList", hospitalList);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("animalList", hospitalService.getAllAnimals());
        String selectedAnimalsString = "";
        if (animal != null && !animal.isEmpty()) {
            selectedAnimalsString = animal.stream()
                                        .map(String::valueOf)
                                        .collect(Collectors.joining(","));
        }
        model.addAttribute("selectedAnimals", animal == null ? new ArrayList<>() : animal); // 기존 리스트도 유지
        model.addAttribute("selectedAnimalsString", selectedAnimalsString); // 새로 추가

        return "service/hospital/hosp_list";
    }

    // 병원 상세 페이지
    @GetMapping("/detail/{id}")
    public String details(@PathVariable Integer id, Model model) {
        model.addAttribute("hospital", hospitalService.getHospital(id));
        model.addAttribute("reviews", hospitalService.getReviews(id));
        model.addAttribute("reviewForm", new HospReviewForm());
        return "service/hospital/details";
    }





    // 병원 등록 폼 페이지
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("hospitalForm", new HospitalForm());
        try {
            model.addAttribute("specialtyList", hospitalService.getAllSpecialties());
            model.addAttribute("animalList", hospitalService.getAllAnimals());
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            // Handle error, e.g., add error message to model
        }
        return "service/hospital/create_hospital";
    }

    // 병원 등록/수정 처리 (AJAX 요청 처리)
    @PostMapping
    @ResponseBody // For AJAX response
    public String processHospitalForm(@Valid @RequestPart("hospitalForm") HospitalForm hospitalForm,
                                      BindingResult bindingResult, // Add BindingResult
                                      @RequestParam(value = "thumbnailImageFile", required = false) MultipartFile thumbnailImageFile) {

        if (bindingResult.hasErrors()) {
            // Log validation errors
            bindingResult.getAllErrors().forEach(error -> System.err.println("Validation Error: " + error.getDefaultMessage()));
            return "{\"status\": \"error\", \"message\": \"Validation failed: " + bindingResult.getFieldError().getDefaultMessage().replace("\"", "") + "\"}";
        }

        try {
            if (hospitalForm.getHospitalId() == null) {
                // Create new hospital
                hospitalService.createHospital(hospitalForm, thumbnailImageFile);
            } else {
                // Update existing hospital
                hospitalService.updateHospital(hospitalForm, thumbnailImageFile);
            }
            return "{\"status\": \"success\", \"message\": \"Hospital data saved successfully.\"}";
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            return "{\"status\": \"error\", \"message\": \"Failed to save hospital data: " + e.getMessage().replace("\"", "") + "\"}";
        }
    }

    // 병원 수정 폼 페이지
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Integer id, Model model) {
        try {
            Hospital hospital = hospitalService.getHospital(id);
            if (hospital == null) {
                // Handle hospital not found, e.g., redirect to error page or list
                return "redirect:/hospitals"; // Or an error page
            }
            // Convert Hospital domain object to HospitalForm for the form
            HospitalForm hospitalForm = HospitalForm.builder()
                                        .hospitalId(hospital.getHospitalId())
                                        .name(hospital.getName())
                                        .address(hospital.getAddress())
                                        .homepage(hospital.getHomepage())
                                        .phone(hospital.getPhone())
                                        .email(hospital.getEmail())
                                        .hospIntroduce(hospital.getHospIntroduce())
                                        .thumbnailImageUrl(hospital.getThumbnailImageUrl())
                                        // Note: specialtyIds and animalIds are not directly in Hospital domain object
                                        // You might need to fetch them separately if they are not populated by MyBatis collection mapping
                                        // For now, assuming they are populated via the resultMap in mapper
                                        .specialtyIds(hospital.getSpecialties() != null ? hospital.getSpecialties().stream().map(s -> s.getSpecialtyId()).collect(java.util.stream.Collectors.toList()) : null)
                                        .animalIds(hospital.getAnimals() != null ? hospital.getAnimals().stream().map(a -> a.getAnimalId()).collect(java.util.stream.Collectors.toList()) : null)
                                        .build();

            model.addAttribute("hospitalForm", hospitalForm);
            model.addAttribute("specialtyList", hospitalService.getAllSpecialties());
            model.addAttribute("animalList", hospitalService.getAllAnimals());
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            // Handle error
            return "redirect:/hospitals"; // Or an error page
        }
        return "service/hospital/create_hospital"; // Re-use the same form for edit
    }

    // 병원 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteHospital(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            hospitalService.deleteHospital(id);
            redirectAttributes.addFlashAttribute("message", "Hospital deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            redirectAttributes.addFlashAttribute("error", "Failed to delete hospital: " + e.getMessage());
        }
        return "redirect:/hospitals"; // Redirect to hospital list page
    }






    // 병원 리뷰 등록 처리
        @PostMapping("/hospitals/{id}/reviews")
    public String addReview(@PathVariable Integer id, @ModelAttribute HospReviewForm form, Principal principal) {
        if (principal == null) {
            // 로그인하지 않은 사용자 처리 (예: 로그인 페이지로 리다이렉트)
            return "redirect:/login"; // 또는 다른 로그인 페이지 경로
        }
        String username = principal.getName(); // 이메일 (username)
        com.aloha.zootopia.domain.Users user = null;
        try {
            user = userMapper.select(username);
        } catch (Exception e) {
            // 사용자 조회 실패 처리
            e.printStackTrace();
            return "redirect:/error"; // 또는 적절한 오류 페이지로 리다이렉트
        }
        if (user == null) {
            return "redirect:/login"; // 사용자 정보가 없으면 로그인 페이지로
        }
        Integer userId = (int) user.getUserId(); // long을 int로 캐스팅
        String nickname = user.getNickname(); // Users 엔티티에 nickname 필드가 있다고 가정

        hospitalService.addReview(id, form, nickname, userId);
        return "redirect:/hospitals/detail/" + id;
    }

    @PostMapping("/hospitals/{id}/reviews/{reviewId}/edit")
    public String updateReview(@PathVariable Integer id,
                               @PathVariable Integer reviewId,
                               @RequestParam String content,
                               Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName(); // 이메일 (username)
        com.aloha.zootopia.domain.Users user = null;
        try {
            user = userMapper.select(username);
        } catch (Exception e) {
            // 사용자 조회 실패 처리
            e.printStackTrace();
            return "redirect:/error"; // 또는 적절한 오류 페이지로 리다이렉트
        }
        if (user == null) {
            return "redirect:/login"; // 사용자 정보가 없으면 로그인 페이지로
        }
        Integer userId = (int) user.getUserId(); // long을 int로 캐스팅

        hospitalService.updateReview(reviewId, content, userId);
        return "redirect:/hospitals/detail/" + id;
    }





}

