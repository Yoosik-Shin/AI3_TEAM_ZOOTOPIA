package com.aloha.zootopia.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.zootopia.domain.ParttimeJob;

@Mapper
public interface ParttimeJobMapper {
    void insertJob(ParttimeJob job);
    List<ParttimeJob> selectAllJobs();
    ParttimeJob selectJobById(int jobId);
    void updateJob(ParttimeJob job);
    void deleteJob(int jobId);
}
