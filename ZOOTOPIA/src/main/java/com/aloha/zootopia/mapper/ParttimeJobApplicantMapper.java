package com.aloha.zootopia.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.zootopia.domain.ParttimeJobApplicant;

@Mapper
public interface ParttimeJobApplicantMapper {
    void insertApplicant(ParttimeJobApplicant applicant);
    List<ParttimeJobApplicant> selectApplicantsByJobId(int jobId);
    void updateApplicant(ParttimeJobApplicant applicant);
    void deleteApplicant(int applicantId);
}
