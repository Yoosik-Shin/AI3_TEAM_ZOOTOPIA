package com.aloha.zootopia.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aloha.zootopia.domain.Hospital;

@Mapper
public interface HospitalMapper {
    List<Hospital> findAll();
    List<Hospital> findByAnimalIds(@Param("animalIds") Map<String, Object> param);
    Hospital findById(@Param("hospitalId") Integer hospitalId);
    void insertHospital(Hospital hospital);
    void insertHospitalAnimal(@Param("hospitalId") Integer hospitalId, @Param("animalId") Integer animalId);
    void insertHospitalSpecialty(@Param("hospitalId") Integer hospitalId, @Param("specialtyId") Integer specialtyId);
}
