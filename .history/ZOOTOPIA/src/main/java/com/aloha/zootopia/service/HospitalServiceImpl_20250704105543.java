package com.aloha.zootopia.service;

import java.util.List;

import com.aloha.zootopia.domain.Hospital;
import com.aloha.zootopia.mapper.HospitalMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class HospitalServiceImpl implements hospitalService {

    private final HospitalMapper hospitalMapper;

    public HospitalServiceImpl(HospitalMapper hospitalMapper) {
        this.hospitalMapper = hospitalMapper;
    }

    @Override
    public PageInfo<Hospital> getHospitalsWithPaging(List<Integer> animal, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Hospital> hospitals = hospitalMapper.selectHospitalsByAnimal(animal);
        return new PageInfo<>(hospitals, 5); // 5는 네비게이션 페이지 개수
    }
    
}
