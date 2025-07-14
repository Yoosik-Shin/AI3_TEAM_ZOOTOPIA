package com.aloha.zootopia.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hospital {
    private Integer hospitalId;
    private String name;
    private String address;
    private String homepage;
    private String phone;
    private String email;
    private List<Animal> animals;
    private List<Specialty> specialties;
    private Double avgRating;
    // getter/setter
}
