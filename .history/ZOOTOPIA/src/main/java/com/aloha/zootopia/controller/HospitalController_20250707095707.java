package com.aloha.zootopia.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aloha.zootopia.domain.Hospital;
import com.aloha.zootopia.dto.HospReviewForm;
import com.aloha.zootopia.dto.HospitalForm;
import com.aloha.zootopia.dto.PageInfo;
import com.aloha.zootopia.mapper.UserMapper;
import com.aloha.zootopia.service.AnimalService;
import com.aloha.zootopia.service.HospitalService;
// import com.github.pagehelper.PageInfo;
import com.aloha.zootopia.service.hospital.HospitalImageUploader;

@Controller
public class HospitalController {
    @Autowired HospitalService hospitalService;
    @Autowired AnimalService animalService;
    @Autowired UserMapper userMapper;
<<<<<<< Updated upstream
=======
    @Autowired HospitalImageUploader hospitalImageUploader;
>>>>>>> Stashed changes

    public HospitalController(HospitalService hospitalService, AnimalService animalService, com.aloha.zootopia.mapper.UserMapper userMapper) {
        this.hospitalService = hospitalService;
        this.animalService = animalService;
    }
<<<<<<< Updated upstream

=======

>>>>>>> Stashed changes
    // @GetMapping("/hospitals")
    // public String list(@RequestParam(required = false) List<Integer> animal, Model model) {
    //     model.addAttribute("animalList", hospitalService.getAllAnimals());
    //     model.addAttribute("selectedAnimals", animal == null ? new ArrayList<>() : animal);
    //     model.addAttribute("hospitals", hospitalService.getHospitals(animal));
    //     return "service/hospital/hosp_list";
    // }

    @GetMapping("/hospitals")
    public String list(
<<<<<<< Updated upstream
        @RequestParam(required = false) List<Integer> animal,
        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
=======
        @RequestParam(required = false) List<Integer> animal,
        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
>>>>>>> Stashed changes
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


    @GetMapping("/hospitals/detail/{id}")
    public String details(@PathVariable Integer id, Model model) {
        model.addAttribute("hospital", hospitalService.getHospital(id));
        model.addAttribute("reviews", hospitalService.getReviews(id));
        model.addAttribute("reviewForm", new HospReviewForm());
        return "service/hospital/details";
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/hospitals/new")
    public String createForm(Model model) {
        model.addAttribute("hospitalForm", new HospitalForm());
        model.addAttribute("specialtyList", hospitalService.getAllSpecialties());
        model.addAttribute("animalList", hospitalService.getAllAnimals());
        return "service/hospital/create_hospital";
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/hospitals")
    public String create(@ModelAttribute HospitalForm form, @RequestParam(value = "thumbnailImageFile", required = false) MultipartFile thumbnailImageFile) throws Exception {
        if (thumbnailImageFile != null && !thumbnailImageFile.isEmpty()) {
            String imageUrl = hospitalImageUploader.uploadFile(thumbnailImageFile);
            form.setThumbnailImageUrl(imageUrl);
        }
        hospitalService.createHospital(form);
        return "redirect:/hospitals";
    }

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