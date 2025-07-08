package com.aloha.zootopia.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aloha.zootopia.domain.Hospital;
import com.aloha.zootopia.dto.HospitalForm;
import com.aloha.zootopia.dto.ReviewForm;
import com.aloha.zootopia.service.HospitalService;

@Controller
public class HospitalController {
    @Autowired HospitalService hospitalService;

    // @GetMapping("/hospitals")
    // public String list(@RequestParam(required = false) List<Integer> animal, Model model) {
    //     model.addAttribute("animalList", hospitalService.getAllAnimals());
    //     model.addAttribute("selectedAnimals", animal == null ? new ArrayList<>() : animal);
    //     model.addAttribute("hospitals", hospitalService.getHospitals(animal));
    //     return "service/hospital/hosp_list";
    // }

    @GetMapping("/hospitals")
    public String list(
        @RequestParam(required = false) List<Integer> animal, Model model) {
        model.addAttribute("animalList", hospitalService.getAllAnimals());
        model.addAttribute("selectedAnimals", animal == null ? new ArrayList<>() : animal);
        model.addAttribute("pageInfo", hospitalService.getHospitals(animal));
        model.addAttribute("pageInfo", hospitalService.getHospitals(animal));
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
