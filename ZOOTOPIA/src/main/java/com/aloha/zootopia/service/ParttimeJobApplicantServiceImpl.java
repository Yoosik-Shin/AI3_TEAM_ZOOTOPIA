package com.aloha.zootopia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.zootopia.domain.ParttimeJobApplicant;
import com.aloha.zootopia.mapper.ParttimeJobApplicantMapper;

@Service
public class ParttimeJobApplicantServiceImpl implements ParttimeJobApplicantService {
    
    @Autowired
    private ParttimeJobApplicantMapper applicantMapper;

    @Override
    public void registerApplicant(ParttimeJobApplicant applicant) {
        applicantMapper.insertApplicant(applicant);
    }

    @Override
    public List<ParttimeJobApplicant> listApplicants(int jobId) {
        return applicantMapper.selectApplicantsByJobId(jobId);
    }

    @Override
    public void updateApplicant(ParttimeJobApplicant applicant) {
        applicantMapper.updateApplicant(applicant);
    }

    @Override
    public void deleteApplicant(int applicantId) {
        applicantMapper.deleteApplicant(applicantId);
    }
}
