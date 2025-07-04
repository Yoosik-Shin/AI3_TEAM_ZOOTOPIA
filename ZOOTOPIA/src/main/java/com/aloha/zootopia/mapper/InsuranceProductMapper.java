package com.aloha.zootopia.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.zootopia.domain.InsuranceProduct;

@Mapper
public interface InsuranceProductMapper {
    
    void insertProduct(InsuranceProduct product);
    List<InsuranceProduct> selectAllProducts();
    InsuranceProduct selectProductById(int productId);
    void updateProduct(InsuranceProduct product);
    void deleteProduct(int productId);
}