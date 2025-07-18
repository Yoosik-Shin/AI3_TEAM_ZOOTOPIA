package com.aloha.zootopia.domain;

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
    private String name;
    private String address;
    private String homepage;
    private String phone;
    private String email;
    private List<Integer> specialtyIds;
    private List<Integer> animalIds;
    // getter/setter
}
