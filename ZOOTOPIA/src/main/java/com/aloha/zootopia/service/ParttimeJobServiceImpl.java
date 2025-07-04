package com.aloha.zootopia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.zootopia.domain.ParttimeJob;
import com.aloha.zootopia.mapper.ParttimeJobMapper;

@Service
public class ParttimeJobServiceImpl implements ParttimeJobService {

    @Autowired
    private ParttimeJobMapper jobMapper;

    @Override
    public void registerJob(ParttimeJob job) {
        jobMapper.insertJob(job);
    }

    @Override
    public List<ParttimeJob> listJobs() {
        return jobMapper.selectAllJobs();
    }

    @Override
    public ParttimeJob getJob(int jobId) {
        return jobMapper.selectJobById(jobId);
    }

    @Override
    public void updateJob(ParttimeJob job) {
        jobMapper.updateJob(job);
    }

    @Override
    public void deleteJob(int jobId) {
        jobMapper.deleteJob(jobId);
    }
    
}
