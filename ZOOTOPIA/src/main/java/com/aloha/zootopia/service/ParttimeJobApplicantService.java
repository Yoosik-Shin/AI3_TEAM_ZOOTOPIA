package com.aloha.zootopia.service;

import java.util.List;

import com.aloha.zootopia.domain.ParttimeJobApplicant;

public interface ParttimeJobApplicantService {
    void registerApplicant(ParttimeJobApplicant applicant);
    List<ParttimeJobApplicant> listApplicants(int jobId);
    void updateApplicant(ParttimeJobApplicant applicant);
    void deleteApplicant(int applicantId);
}
