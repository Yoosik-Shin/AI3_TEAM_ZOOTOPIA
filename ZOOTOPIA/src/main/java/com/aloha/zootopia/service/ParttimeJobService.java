package com.aloha.zootopia.service;

import java.util.List;

import com.aloha.zootopia.domain.ParttimeJob;


public interface ParttimeJobService {
    void registerJob(ParttimeJob job);
    List<ParttimeJob> listJobs();
    ParttimeJob getJob(int jobId);
    void updateJob(ParttimeJob job);
    void deleteJob(int jobId);
}
