package com.aloha.zootopia.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aloha.zootopia.domain.Hospital;
import com.aloha.zootopia.dto.HospitalForm;
import com.aloha.zootopia.dto.PageInfo;
import com.aloha.zootopia.dto.ReviewForm;
import com.aloha.zootopia.service.AnimalService;
import com.aloha.zootopia.service.HospitalService;
// import com.github.pagehelper.PageInfo;

@Controller
public class HospitalController {
    @Autowired HospitalService hospitalService;
    @Autowired AnimalService animalService;

    public HospitalController(HospitalService hospitalService, AnimalService animalService) {
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

    @GetMapping("/hospitals")
    public String list(
        @RequestParam(required = false) List<Integer> animal, 
        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, 
        Model model) {

        int pageSize = 6;
        int total = hospitalService.getHospitalCount(animal);
        List<Hospital> hospitals = hospitalService.getHospitalList(animal, pageNum, pageSize);

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
        pageInfo.setHasFirstPage(startPage > 1);
        pageInfo.setHasLastPage(endPage < pages);
        
        model.addAttribute("animalList", hospitalService.getAllAnimals());
        model.addAttribute("selectedAnimals", animal == null ? new ArrayList<>() : animal);
        model.addAttribute("hospitals", hospitalService.getHospitals(animal));
        return "service/hospital/hosp_list";
    }


    @GetMapping("/hospitals/{id}")
    public String details(@PathVariable Integer id, Model model) {
        model.addAttribute("hospital", hospitalService.getHospital(id));
        model.addAttribute("reviews", hospitalService.getReviews(id));
        model.addAttribute("reviewForm", new ReviewForm());
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
    public String create(@ModelAttribute HospitalForm form) {
        hospitalService.createHospital(form);
        return "redirect:/hospitals";
    }

    @PostMapping("/hospitals/{id}/reviews")
    public String addReview(@PathVariable Integer id, @ModelAttribute ReviewForm form, Principal principal) {
        // principal.getName() → userId, nickname은 사용자 세션에서 가져오도록 구현 필요
        hospitalService.addReview(id, form, principal.getName(), /*userId*/ 1);
        return "redirect:/hospitals/" + id;
    }

    

}
