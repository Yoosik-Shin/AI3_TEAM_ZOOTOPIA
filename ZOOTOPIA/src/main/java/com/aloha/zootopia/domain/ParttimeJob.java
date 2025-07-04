package com.aloha.zootopia.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ParttimeJob {
    private int jobId;
    private String title;
    private String location;
    private int pay;
    private LocalDateTime workDate;
    private int userId;
    private String petInfo;
    private String memo;
    private LocalDateTime createdAt;
}
