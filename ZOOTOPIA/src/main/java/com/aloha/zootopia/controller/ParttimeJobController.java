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

import com.aloha.zootopia.domain.ParttimeJob;
import com.aloha.zootopia.service.ParttimeJobService;

@Controller
@RequestMapping("/parttime/job")
public class ParttimeJobController {
    
    @Autowired
    private ParttimeJobService jobService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("jobs", jobService.listJobs());
        return "parttime/list";
    }

    @GetMapping("/register")
    @PreAuthorize("hasRole('USER')")
    public String showRegisterForm() {
        return "parttime/register";
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('USER')")
    public String registerJob(ParttimeJob job, @AuthenticationPrincipal User user) {
        job.setUserId(Integer.parseInt(user.getUsername())); // 로그인 ID
        jobService.registerJob(job);
        return "redirect:/parttime/list";
    }

    @GetMapping("/detail/{jobId}")
    public String detail(@PathVariable int jobId, Model model) {
        model.addAttribute("job", jobService.getJob(jobId));
        return "parttime/detail";
    }

    @GetMapping("/update/{jobId}")
    @PreAuthorize("hasRole('USER')")
    public String showUpdateForm(@PathVariable int jobId, Model model, @AuthenticationPrincipal User user) {
        ParttimeJob job = jobService.getJob(jobId);
        if (job.getUserId() != Integer.parseInt(user.getUsername())) {
            throw new SecurityException("작성자만 수정할 수 있습니다.");
        }
        model.addAttribute("job", job);
        return "parttime/update";
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public String updateJob(ParttimeJob job, @AuthenticationPrincipal User user) {
        if (job.getUserId() != Integer.parseInt(user.getUsername())) {
            throw new SecurityException("작성자만 수정할 수 있습니다.");
        }
        jobService.updateJob(job);
        return "redirect:/parttime/detail/" + job.getJobId();
    }

    @PostMapping("/delete/{jobId}")
    @PreAuthorize("hasRole('USER')")
    public String deleteJob(@PathVariable int jobId, @AuthenticationPrincipal User user) {
        ParttimeJob job = jobService.getJob(jobId);
        if (job.getUserId() != Integer.parseInt(user.getUsername())) {
            throw new SecurityException("작성자만 삭제할 수 있습니다.");
        }
        jobService.deleteJob(jobId);
        return "redirect:/parttime/list";
    }

}
