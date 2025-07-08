package com.aloha.zootopia.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aloha.zootopia.domain.Hospital;
import com.aloha.zootopia.dto.HospReviewForm;
import com.aloha.zootopia.dto.HospitalForm;
import com.aloha.zootopia.dto.PageInfo;
import com.aloha.zootopia.mapper.UserMapper;
import com.aloha.zootopia.service.AnimalService;
// import com.github.pagehelper.PageInfo;
import com.aloha.zootopia.service.hospital.HospitalImageUploader;
import com.aloha.zootopia.service.hospital.HospitalService;

@Controller
public class HospitalController {
    @Autowired HospitalService hospitalService;
    @Autowired AnimalService animalService;
    @Autowired UserMapper userMapper;
    @Autowired HospitalImageUploader hospitalImageUploader;

    public HospitalController(HospitalService hospitalService, AnimalService animalService, com.aloha.zootopia.mapper.UserMapper userMapper) {
        this.hospitalService = hospitalService;
        this.animalService = animalService;
    }
    // @GetMapping("/hospitals")
    // public String list(@RequestParam(required = false) List<Integer> animal, Model model) {
    //     model.addAttribute("animalList", hospitalService.getAllAnimals());
    //     model.addAttribute("selectedAnimals", animal == null ? new ArrayList<>() : animal);
    //     model.addAttribute("hospitals", hospitalService.getHospitals(animal));
    //     return "service/hospital/hosp_list";
    // }

    /**
     * 인증 테스트용 메서드
     * @return
     */
    @GetMapping("/auth-test")
     @ResponseBody // 페이지 이동 없이 응답을 바로 확인하기 위함
    public String authTest() {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser"
    )) {
            return "로그인되지 않은 사용자입니다.";
    }

        String username = auth.getName();
        java.util.Collection<? extends org.springframework.security.core.GrantedAuthority>
        authorities = auth.getAuthorities();

        System.out.println("--- 권한 테스트 ---");
        System.out.println("사용자: " + username);
        System.out.println("권한: " + authorities);
        System.out.println("-------------------");

        return "사용자: " + username + " / 권한: " + authorities;
    }



    @GetMapping("/hospitals")
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


    @GetMapping("/hospitals/detail/{id}")
    public String details(@PathVariable Integer id, Model model) {
        model.addAttribute("hospital", hospitalService.getHospital(id));
        model.addAttribute("reviews", hospitalService.getReviews(id));
        model.addAttribute("reviewForm", new HospReviewForm());
        return "service/hospital/details";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/hospitals/new")
    public String createForm(Model model) {

        // 인증 객체에서 권한 정보 로그 출력
        org.springframework.security.core.Authentication auth =
            org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        System.out.println("현재 로그인 사용자: " + auth.getName());
        System.out.println("권한 목록: " + auth.getAuthorities());

        model.addAttribute("hospitalForm", new HospitalForm());
        model.addAttribute("specialtyList", hospitalService.getAllSpecialties());
        model.addAttribute("animalList", hospitalService.getAllAnimals());
        return "service/hospital/create_hospital";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/hospitals/new")
    public ResponseEntity<?> create(@ModelAttribute HospitalForm form) throws Exception {
        hospitalService.createHospital(form);
        return ResponseEntity.ok().body(Map.of("message", "병원 정보가 성공적으로 등록되었습니다."));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/hospitals/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Hospital hospital = hospitalService.getHospital(id);
        HospitalForm hospitalForm = new HospitalForm();
        // Hospital 객체의 데이터를 HospitalForm으로 복사
        hospitalForm.setHospitalId(hospital.getHospitalId());
        hospitalForm.setName(hospital.getName());
        hospitalForm.setAddress(hospital.getAddress());
        hospitalForm.setHomepage(hospital.getHomepage());
        hospitalForm.setPhone(hospital.getPhone());
        hospitalForm.setEmail(hospital.getEmail());
        hospitalForm.setThumbnailImageUrl(hospital.getThumbnailImageUrl());

        // 진료 과목 및 진료 가능 동물 ID 리스트 설정
        if (hospital.getSpecialties() != null) {
            hospitalForm.setSpecialtyIds(hospital.getSpecialties().stream()
                                                .map(s -> s.getSpecialtyId())
                                                .collect(Collectors.toList()));
        }
        if (hospital.getAnimals() != null) {
            hospitalForm.setAnimalIds(hospital.getAnimals().stream()
                                            .map(a -> a.getAnimalId())
                                            .collect(Collectors.toList()));
        }

        model.addAttribute("hospitalForm", hospitalForm);
        model.addAttribute("specialtyList", hospitalService.getAllSpecialties());
        model.addAttribute("animalList", hospitalService.getAllAnimals());
        return "service/hospital/create_hospital"; // create_hospital.html 재사용
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/hospitals/edit")
    public ResponseEntity<?> update(@ModelAttribute HospitalForm form) throws Exception {
        hospitalService.updateHospital(form); // HospitalService에 updateHospital 메서드 필요
        return ResponseEntity.ok().body(Map.of("message", "병원 정보가 성공적으로 수정되었습니다."));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/hospitals/delete/{id}")
    public String delete(@PathVariable Integer id) {
        hospitalService.deleteHospital(id); // HospitalService에 deleteHospital 메서드 필요
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

    @PreAuthorize("permitAll()")
    @PostMapping("/upload/image")
    @ResponseBody
    public Map<String, Object> uploadImage(@RequestParam("image") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + "_" + file.getOriginalFilename();
            String savePath = "C:/upload/" + fileName; // 실제 운영 환경에서는 이 경로를 변경해야 합니다.
            file.transferTo(new File(savePath));
            result.put("success", 1);
            result.put("imageUrl", "/upload/" + fileName); // 에디터에 삽입될 이미지 URL
        } catch (Exception e) {
            result.put("success", 0);
            result.put("message", "업로드 실패");
            e.printStackTrace(); // 에러 로깅
        }
        return result;
    }
}