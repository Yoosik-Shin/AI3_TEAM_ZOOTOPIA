package com.aloha.zootopia.controller;

import com.aloha.zootopia.domain.Hospital;
import com.aloha.zootopia.dto.HospitalForm;
import com.aloha.zootopia.service.hospital.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // Import BindingResult
import jakarta.validation.Valid; // Import @Valid
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List; // Assuming specialtyList and animalList are Lists

@Controller
@RequestMapping("/hospitals")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

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
    public String processHospitalForm(@Valid @ModelAttribute HospitalForm hospitalForm,
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
    @GetMapping("/{id}/edit")
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

}

