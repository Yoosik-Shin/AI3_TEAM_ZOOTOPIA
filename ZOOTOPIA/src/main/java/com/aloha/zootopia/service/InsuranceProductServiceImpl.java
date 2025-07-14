package com.aloha.zootopia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.zootopia.domain.InsuranceProduct;
import com.aloha.zootopia.mapper.InsuranceProductMapper;

@Service
public class InsuranceProductServiceImpl implements InsuranceProductService {

    @Autowired
    private InsuranceProductMapper productMapper;

    @Override
    public void registerProduct(InsuranceProduct product) {
        productMapper.insertProduct(product);
    }

    @Override
    public List<InsuranceProduct> listProducts() {
        return productMapper.selectAllProducts();
    }

    @Override
    public InsuranceProduct getProduct(int productId) {
        return productMapper.selectProductById(productId);
    }

    @Override
    public void updateProduct(InsuranceProduct product) {
        productMapper.updateProduct(product);
    }

    @Override
    public void deleteProduct(int productId) {
        productMapper.deleteProduct(productId);
    }
    
    // ✅ 페이징 처리된 상품 목록 조회
    @Override
    public List<InsuranceProduct> getProductsPaged(int offset, int size) {
        return productMapper.selectProductsPaged(offset, size);
    }

    // ✅ 전체 상품 개수 조회
    @Override
    public int getTotalCount() {
        return productMapper.countAllProducts();
    }
}
