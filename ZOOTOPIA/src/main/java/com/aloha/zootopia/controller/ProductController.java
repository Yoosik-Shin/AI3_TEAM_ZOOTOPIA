package com.aloha.zootopia.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.aloha.zootopia.domain.Product;
import com.aloha.zootopia.domain.Pagination;
import com.aloha.zootopia.service.ProductService;
import com.aloha.zootopia.service.FileUploadService;

@Controller
@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private FileUploadService fileUploadService;
    
    // 테스트용 API 엔드포인트
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Product Controller is working!";
    }
    
    // 테스트용 단순 상품 목록 페이지
    @GetMapping("")
    public String productHome() {
        return "redirect:/products/list";
    }
    
    // 테스트용 단순 상품 목록 (에러 처리 포함)
    @GetMapping("/list")
    public String list(Model model, 
                      @RequestParam(defaultValue = "1") int page,
                      @RequestParam(defaultValue = "9") int size,
                      @RequestParam(required = false) String category,
                      @RequestParam(required = false) String search) {
        
        try {
            List<Product> productList;
            Pagination pagination;
            
            // 기본 더미 데이터 생성 (DB 연결 실패 시 대비)
            if (productService == null) {
                productList = createDummyProducts();
                pagination = new Pagination(page, size, 10, productList.size());
            } else {
                // 검색이 있는 경우
                if (search != null && !search.trim().isEmpty()) {
                    productList = productService.searchByName(search, page, size);
                    pagination = productService.getSearchPagination(page, size, search);
                    model.addAttribute("search", search);
                }
                // 카테고리 필터가 있는 경우
                else if (category != null && !category.trim().isEmpty()) {
                    productList = productService.listByCategoryAndPage(category, page, size);
                    pagination = productService.getPagination(page, size, category);
                    model.addAttribute("category", category);
                }
                // 전체 목록
                else {
                    productList = productService.listByPage(page, size);
                    pagination = productService.getPagination(page, size, null);
                }
            }
            
            model.addAttribute("productList", productList);
            model.addAttribute("pagination", pagination);
            
        } catch (Exception e) {
            // 에러 발생 시 더미 데이터로 대체
            List<Product> dummyProducts = createDummyProducts();
            Pagination dummyPagination = new Pagination(page, size, 10, dummyProducts.size());
            
            model.addAttribute("productList", dummyProducts);
            model.addAttribute("pagination", dummyPagination);
            model.addAttribute("error", "데이터를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return "products/list";
    }
    
    // 상품 상세 페이지
    @GetMapping("/detail")
    public String detail(@RequestParam int no, Model model) {
        Product product = productService.getByNo(no);
        model.addAttribute("product", product);
        return "products/detail";
    }
    
    // 좋아요 기능
    @PostMapping("/like/{no}")
    public ResponseEntity<Map<String, Object>> likeProduct(@PathVariable int no) {
        Map<String, Object> response = new HashMap<>();
        try {
            int result = productService.updateLikes(no);
            if (result > 0) {
                Product product = productService.getByNo(no);
                response.put("success", true);
                response.put("likes", product.getLikes());
            } else {
                response.put("success", false);
                response.put("message", "좋아요 처리에 실패했습니다.");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "오류가 발생했습니다.");
        }
        return ResponseEntity.ok(response);
    }
    
    // 상품 등록 페이지
    @GetMapping("/create")
    public String createForm(Model model) {
        // 관리자 권한 확인 (임시로 주석 처리 - 테스트용)
        // if (!isAdmin()) {
        //     model.addAttribute("error", "관리자만 상품을 등록할 수 있습니다.");
        //     return "redirect:/products/list";
        // }
        
        model.addAttribute("product", new Product());
        return "products/create";
    }
    
    // 상품 등록 처리
    @PostMapping("/create")
    public String create(Product product, 
                        @RequestParam(required = false) MultipartFile imageFile,
                        RedirectAttributes redirectAttributes) {
        try {
            // 관리자 권한 확인 (임시로 주석 처리 - 테스트용)
            // if (!isAdmin()) {
            //     redirectAttributes.addFlashAttribute("error", "관리자만 상품을 등록할 수 있습니다.");
            //     return "redirect:/products/list";
            // }
            
            // 이미지 파일 업로드 처리
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = fileUploadService.uploadFile(imageFile);
                product.setImageUrl(imageUrl);
            } else {
                product.setImageUrl("/img/default-thumbnail.png");
            }
            
            // 상품 등록
            int result = productService.insert(product);
            
            if (result > 0) {
                redirectAttributes.addFlashAttribute("success", "상품이 성공적으로 등록되었습니다.");
                return "redirect:/products/list";
            } else {
                redirectAttributes.addFlashAttribute("error", "상품 등록에 실패했습니다.");
                return "redirect:/products/create";
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "오류가 발생했습니다: " + e.getMessage());
            return "redirect:/products/create";
        }
    }
    
    // 관리자 권한 확인
    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            return "super@admin.com".equals(username);
        }
        return false;
    }
    
    // 더미 데이터 생성 메서드
    private List<Product> createDummyProducts() {
        List<Product> dummyList = new java.util.ArrayList<>();
        
        for (int i = 1; i <= 9; i++) {
            Product product = new Product();
            product.setNo(i);
            product.setName("샘플 상품 " + i);
            product.setCategory(i % 2 == 0 ? "개" : "고양이");
            product.setDescription("이것은 샘플 상품 " + i + "의 설명입니다.");
            product.setPrice(100000 + (i * 50000));
            product.setImageUrl("/img/default-thumbnail.png");
            product.setStatus("판매중");
            product.setStock(10);
            product.setViews(i * 5);
            product.setLikes(i * 2);
            dummyList.add(product);
        }
        
        return dummyList;
    }
}
