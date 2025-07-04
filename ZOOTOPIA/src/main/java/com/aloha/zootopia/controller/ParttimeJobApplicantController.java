package com.aloha.zootopia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aloha.zootopia.domain.ParttimeJobApplicant;
import com.aloha.zootopia.service.ParttimeJobApplicantService;

@Controller
@RequestMapping("/parttime/job/applicant")
public class ParttimeJobApplicantController {
    
    @Autowired
    private ParttimeJobApplicantService applicantService;


    @PostMapping("/apply")
    @PreAuthorize("hasRole('USER')")
    public String apply(ParttimeJobApplicant applicant, @AuthenticationPrincipal User user) {
        applicant.setUserId(Integer.parseInt(user.getUsername()));
        applicantService.registerApplicant(applicant);
        return "redirect:/parttime/detail/" + applicant.getJobId();
    }

    @PostMapping("/delete/{applicantId}")
    @PreAuthorize("hasRole('USER')")
    public String delete(@PathVariable int applicantId, @RequestParam int jobId, @AuthenticationPrincipal User user) {
        // 글 작성자가 본인인지 검사
        // 실제로는 DB에서 조회 후 비교
        applicantService.deleteApplicant(applicantId);
        return "redirect:/parttime/detail/" + jobId;
    }

    @GetMapping("/list/{jobId}")
    @PreAuthorize("hasRole('USER')")
    public String listApplicants(@PathVariable int jobId, Model model) {
        model.addAttribute("applicants", applicantService.listApplicants(jobId));
        return "parttime/applicants";
    }
}
