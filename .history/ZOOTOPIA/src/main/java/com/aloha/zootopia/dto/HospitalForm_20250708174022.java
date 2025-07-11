package com.aloha.zootopia.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalForm {
    private Integer hospitalId;
    private String name;
    private String address;
    private String homepage;
    private String phone;
    private String email;
    private String hospIntroduce; // 250708 추가
    private String thumbnailImageUrl; // 추가될 필드
    private List<Integer> specialtyIds;
    private List<Integer> animalIds;
    // getter/setter
}
