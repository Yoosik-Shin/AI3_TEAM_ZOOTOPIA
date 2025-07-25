package com.aloha.zootopia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.zootopia.domain.Pagination;
import com.aloha.zootopia.domain.Product;
import com.aloha.zootopia.mapper.ProductMapper;

@Service
public class ProductService {
    
    @Autowired
    private ProductMapper productMapper;
    
    // 상품 목록 조회 (페이징)
    public List<Product> listByPage(int page, int size) {
        int offset = (page - 1) * size;
        return productMapper.selectByPage(offset, size);
    }
    
    // 카테고리별 상품 목록 조회 (페이징)
    public List<Product> listByCategoryAndPage(String category, int page, int size) {
        int offset = (page - 1) * size;
        return productMapper.selectByCategoryAndPage(category, offset, size);
    }
    
    // 페이지네이션 정보 생성
    public Pagination getPagination(int page, int size, String category) {
        int total = (category == null || category.isEmpty()) ? 
                   productMapper.selectTotal() : 
                   productMapper.selectTotalByCategory(category);
        
        return new Pagination(page, size, 10, total);
    }
    
    // 검색 페이지네이션 정보 생성
    public Pagination getSearchPagination(int page, int size, String name) {
        int total = productMapper.selectTotalByName(name);
        return new Pagination(page, size, 10, total);
    }
    
    // 상품 상세 조회
    public Product getByNo(int no) {
        // 조회수 증가
        productMapper.updateViews(no);
        return productMapper.selectByNo(no);
    }
    
    // 상품 등록
    public int insert(Product product) {
        return productMapper.insert(product);
    }
    
    // 상품 수정
    public int update(Product product) {
        return productMapper.update(product);
    }
    
    // 상품 삭제
    public int delete(int no) {
        return productMapper.delete(no);
    }
    
    // 좋아요 수 증가
    public int updateLikes(int no) {
        return productMapper.updateLikes(no);
    }
    
    // 싫어요 수 증가
    public int updateDislikes(int no) {
        return productMapper.updateDislikes(no);
    }
    
    // 검색 (기존 메서드)
    public List<Product> searchByNameWithPage(String name, int page, int size) {
        int offset = (page - 1) * size;
        return productMapper.selectByNameAndPage(name, offset, size);
    }
    
    // 컨트롤러에서 사용하는 메서드들 추가
    
    // 페이지별 상품 조회 (offset 직접 사용)
    public List<Product> getByPage(int offset, int size) {
        return productMapper.selectByPage(offset, size);
    }
    
    // 카테고리별 상품 조회 (offset 직접 사용)
    public List<Product> getByCategory(String category, int offset, int size) {
        return productMapper.selectByCategoryAndPage(category, offset, size);
    }
    
    // 검색 결과 조회 (offset 직접 사용)
    public List<Product> searchByName(String name, int offset, int size) {
        return productMapper.selectByNameAndPage(name, offset, size);
    }
    
    // 전체 상품 수 조회
    public int getTotal() {
        return productMapper.selectTotal();
    }
    
    // 카테고리별 상품 수 조회
    public int getTotalByCategory(String category) {
        return productMapper.selectTotalByCategory(category);
    }
    
    // 검색 결과 상품 수 조회
    public int getTotalByName(String name) {
        return productMapper.selectTotalByName(name);
    }
}
