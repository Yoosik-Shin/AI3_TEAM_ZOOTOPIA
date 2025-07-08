// package com.aloha.zootopia.controller;

// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.ResponseBody;
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;
// import com.aloha.zootopia.domain.Product;
// import com.aloha.zootopia.domain.Pagination;
// import com.aloha.zootopia.service.ProductService;
// import com.aloha.zootopia.service.FileUploadService;

// @Controller
// @RequestMapping("/products")
// public class ProductController {
    
//     @Autowired
//     private ProductService productService;
    
//     @Autowired
//     private FileUploadService fileUploadService;
    
//     // 테스트용 API 엔드포인트
//     @GetMapping("/test")
//     @ResponseBody
//     public String test() {
//         return "Product Controller is working!";
//     }
    
//     // 테스트용 단순 상품 목록 페이지
//     @GetMapping("")
//     public String productHome() {
//         return "redirect:/products/list";
//     }
    
//     // 테스트용 단순 상품 목록 (에러 처리 포함)
//     @GetMapping("/list")
//     public String list(Model model, 
//                       @RequestParam(defaultValue = "1") int page,
//                       @RequestParam(defaultValue = "9") int size,
//                       @RequestParam(required = false) String category,
//                       @RequestParam(required = false) String search) {
        
//         try {
//             List<Product> productList;
//             Pagination pagination;
            
//             // ProductService가 null인지 확인
//             if (productService == null) {
//                 System.out.println("ERROR: ProductService is null!");
//                 productList = createDummyProducts();
//                 pagination = new Pagination(page, size, 10, productList.size());
//                 model.addAttribute("error", "ProductService를 찾을 수 없습니다. 더미 데이터를 표시합니다.");
//             } else {
//                 System.out.println("ProductService is available, fetching real data...");
//                 // 검색이 있는 경우
//                 if (search != null && !search.trim().isEmpty()) {
//                     productList = productService.searchByName(search, page, size);
//                     pagination = productService.getSearchPagination(page, size, search);
//                     model.addAttribute("search", search);
//                 }
//                 // 카테고리 필터가 있는 경우
//                 else if (category != null && !category.trim().isEmpty()) {
//                     productList = productService.listByCategoryAndPage(category, page, size);
//                     pagination = productService.getPagination(page, size, category);
//                     model.addAttribute("category", category);
//                 }
//                 // 전체 목록
//                 else {
//                     productList = productService.listByPage(page, size);
//                     pagination = productService.getPagination(page, size, null);
//                 }
                
//                 System.out.println("Fetched " + productList.size() + " products from database");
//                 for (Product p : productList) {
//                     System.out.println("Product: " + p.getName() + " - Image: " + p.getImageUrl());
//                 }
//             }
            
//             model.addAttribute("productList", productList);
//             model.addAttribute("pagination", pagination);
            
//         } catch (Exception e) {
//             // 에러 발생 시 더미 데이터로 대체
//             List<Product> dummyProducts = createDummyProducts();
//             Pagination dummyPagination = new Pagination(page, size, 10, dummyProducts.size());
            
//             model.addAttribute("productList", dummyProducts);
//             model.addAttribute("pagination", dummyPagination);
//             model.addAttribute("error", "데이터를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
//         }
        
//         return "products/list";
//     }
    
//     // 상품 상세 페이지
//     @GetMapping("/detail/{id}")
//     public String detail(@PathVariable("id") int id, Model model) {
//         try {
//             // 상품 정보 조회
//             Product product = productService.getByNo(id);
//             if (product == null) {
//                 model.addAttribute("error", "상품을 찾을 수 없습니다.");
//                 return "error/404";
//             }
            
//             // 조회수 증가 (추후 ProductService에 updateViews 메서드 추가 예정)
//             try {
//                 // productService.updateViews(id);
//                 System.out.println("상품 " + id + " 조회수 증가 (기능 미구현)");
//             } catch (Exception e) {
//                 System.out.println("조회수 업데이트 실패: " + e.getMessage());
//             }
            
//             // 리뷰 정보 조회 (임시로 더미 데이터 사용)
//             product.setRating(4.5);
//             product.setReviewCount(15);
            
//             // 더미 리뷰 목록 생성
//             java.util.List<com.aloha.zootopia.domain.Review> reviews = new java.util.ArrayList<>();
//             com.aloha.zootopia.domain.Review review1 = new com.aloha.zootopia.domain.Review();
//             review1.setUserName("didi****");
//             review1.setRating(5);
//             review1.setContent("정말 좋은 상품이에요!");
//             review1.setCreatedDate(java.time.LocalDateTime.now().minusDays(2));
            
//             com.aloha.zootopia.domain.Review review2 = new com.aloha.zootopia.domain.Review();
//             review2.setUserName("hyel****");
//             review2.setRating(4);
//             review2.setContent("상세히 봐도 좋네요.");
//             review2.setCreatedDate(java.time.LocalDateTime.now().minusDays(5));
            
//             reviews.add(review1);
//             reviews.add(review2);
            
//             model.addAttribute("product", product);
//             model.addAttribute("reviews", reviews);
//             return "products/detail";
            
//         } catch (Exception e) {
//             e.printStackTrace();
//             model.addAttribute("error", "상품 정보를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
//             return "error/500";
//         }
//     }
    
//     // 좋아요/싫어요 토글 기능
//     @PostMapping("/toggle-like/{no}")
//     @ResponseBody
//     public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable int no, @RequestParam String type) {
//         Map<String, Object> response = new HashMap<>();
//         try {
//             // 로그인 확인
//             Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//             if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
//                 response.put("success", false);
//                 response.put("message", "로그인이 필요합니다.");
//                 return ResponseEntity.ok(response);
//             }
            
//             int result = 0;
//             if ("like".equals(type)) {
//                 result = productService.updateLikes(no);
//             } else if ("dislike".equals(type)) {
//                 result = productService.updateDislikes(no);
//             }
            
//             if (result > 0) {
//                 Product product = productService.getByNo(no);
//                 response.put("success", true);
//                 response.put("likes", product.getLikes());
//                 response.put("dislikes", product.getDislikes());
//                 response.put("message", "like".equals(type) ? "좋아요!" : "싫어요가 반영되었습니다.");
//             } else {
//                 response.put("success", false);
//                 response.put("message", "처리에 실패했습니다.");
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//             response.put("success", false);
//             response.put("message", "오류가 발생했습니다: " + e.getMessage());
//         }
//         return ResponseEntity.ok(response);
//     }
    
//     // 상품 등록 페이지
//     @GetMapping("/create")
//     public String createForm(Model model) {
//         // 관리자 권한 확인 (임시로 주석 처리 - 테스트용)
//         // if (!isAdmin()) {
//         //     model.addAttribute("error", "관리자만 상품을 등록할 수 있습니다.");
//         //     return "redirect:/products/list";
//         // }
        
//         model.addAttribute("product", new Product());
//         return "products/create";
//     }
    
//     // 상품 등록 처리
//     @PostMapping("/create")
//     public String create(Product product, 
//                         @RequestParam(required = false) MultipartFile imageFile,
//                         RedirectAttributes redirectAttributes) {
//         try {
//             // 관리자 권한 확인 (임시로 주석 처리 - 테스트용)
//             // if (!isAdmin()) {
//             //     redirectAttributes.addFlashAttribute("error", "관리자만 상품을 등록할 수 있습니다.");
//             //     return "redirect:/products/list";
//             // }
            
//             // 이미지 파일 업로드 처리
//             if (imageFile != null && !imageFile.isEmpty()) {
//                 String imageUrl = fileUploadService.uploadFile(imageFile);
//                 product.setImageUrl(imageUrl);
//             } else {
//                 product.setImageUrl("/img/default-thumbnail.png");
//             }
            
//             // 상품 등록
//             int result = productService.insert(product);
            
//             if (result > 0) {
//                 redirectAttributes.addFlashAttribute("success", "상품이 성공적으로 등록되었습니다.");
//                 return "redirect:/products/list";
//             } else {
//                 redirectAttributes.addFlashAttribute("error", "상품 등록에 실패했습니다.");
//                 return "redirect:/products/create";
//             }
            
//         } catch (Exception e) {
//             redirectAttributes.addFlashAttribute("error", "오류가 발생했습니다: " + e.getMessage());
//             return "redirect:/products/create";
//         }
//     }
    
        // 장바구니 추가 기능
//     @PostMapping("/add-to-cart")
//     @ResponseBody
//     public ResponseEntity<Map<String, Object>> addToCart(@RequestParam int productNo, 
//                                                         @RequestParam String option,
//                                                         @RequestParam int quantity) {
//         Map<String, Object> response = new HashMap<>();
//         try {
//             // 로그인 확인
//             Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//             if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
//                 response.put("success", false);
//                 response.put("message", "로그인이 필요합니다.");
//                 return ResponseEntity.ok(response);
//             }
            
//             // 상품 존재 확인
//             Product product = productService.getByNo(productNo);
//             if (product == null) {
//                 response.put("success", false);
//                 response.put("message", "상품을 찾을 수 없습니다.");
//                 return ResponseEntity.ok(response);
//             }
            
//             // 재고 확인
//             if (product.getStock() < quantity) {
//                 response.put("success", false);
//                 response.put("message", "재고가 부족합니다.");
//                 return ResponseEntity.ok(response);
//             }
            
//             // 장바구니 추가 로직 (추후 CartService 구현)
//             // cartService.addToCart(username, productNo, option, quantity);
            
//             response.put("success", true);
//             response.put("message", "장바구니에 추가되었습니다.");
//             System.out.println("장바구니 추가: " + auth.getName() + " - 상품 " + productNo + " (옵션: " + option + ", 수량: " + quantity + ")");
            
//         } catch (Exception e) {
//             e.printStackTrace();
//             response.put("success", false);
//             response.put("message", "오류가 발생했습니다: " + e.getMessage());
//         }
//         return ResponseEntity.ok(response);
//     }
    
//     // 바로 구매 기능
//     @PostMapping("/buy-now")
//     @ResponseBody
//     public ResponseEntity<Map<String, Object>> buyNow(@RequestParam int productNo,
//                                                      @RequestParam String option,
//                                                      @RequestParam int quantity) {
//         Map<String, Object> response = new HashMap<>();
//         try {
//             // 로그인 확인
//             Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//             if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
//                 response.put("success", false);
//                 response.put("message", "로그인이 필요합니다.");
//                 return ResponseEntity.ok(response);
//             }
            
//             // 상품 존재 확인
//             Product product = productService.getByNo(productNo);
//             if (product == null) {
//                 response.put("success", false);
//                 response.put("message", "상품을 찾을 수 없습니다.");
//                 return ResponseEntity.ok(response);
//             }
            
//             // 재고 확인
//             if (product.getStock() < quantity) {
//                 response.put("success", false);
//                 response.put("message", "재고가 부족합니다.");
//                 return ResponseEntity.ok(response);
//             }
            
//             // 주문 페이지로 리다이렉트할 URL 생성 (추후 OrderController 구현)
//             String orderUrl = "/orders/create?productNo=" + productNo + "&option=" + option + "&quantity=" + quantity;
            
//             response.put("success", true);
//             response.put("message", "주문 페이지로 이동합니다.");
//             response.put("redirectUrl", orderUrl);
//             System.out.println("바로구매: " + auth.getName() + " - 상품 " + productNo + " (옵션: " + option + ", 수량: " + quantity + ")");
            
//         } catch (Exception e) {
//             e.printStackTrace();
//             response.put("success", false);
//             response.put("message", "오류가 발생했습니다: " + e.getMessage());
//         }
//         return ResponseEntity.ok(response);
//     }
    
//     // 리뷰 작성 기능
//     @PostMapping("/add-review")
//     @ResponseBody
//     public ResponseEntity<Map<String, Object>> addReview(@RequestParam int productNo,
//                                                         @RequestParam int rating,
//                                                         @RequestParam String content) {
//         Map<String, Object> response = new HashMap<>();
//         try {
//             // 로그인 확인
//             Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//             if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
//                 response.put("success", false);
//                 response.put("message", "로그인이 필요합니다.");
//                 return ResponseEntity.ok(response);
//             }
            
//             // 상품 존재 확인
//             Product product = productService.getByNo(productNo);
//             if (product == null) {
//                 response.put("success", false);
//                 response.put("message", "상품을 찾을 수 없습니다.");
//                 return ResponseEntity.ok(response);
//             }
            
//             // 리뷰 추가 로직 (추후 ReviewService 구현)
//             // reviewService.addReview(auth.getName(), productNo, rating, content);
            
//             response.put("success", true);
//             response.put("message", "리뷰가 등록되었습니다.");
//             System.out.println("리뷰 등록: " + auth.getName() + " - 상품 " + productNo + " (평점: " + rating + ")");
            
//         } catch (Exception e) {
//             e.printStackTrace();
//             response.put("success", false);
//             response.put("message", "오류가 발생했습니다: " + e.getMessage());
//         }
//         return ResponseEntity.ok(response);
//     }
    
//     // 관리자 권한 확인
//     private boolean isAdmin() {
//         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//         if (auth != null && auth.isAuthenticated()) {
//             String username = auth.getName();
//             return "super@admin.com".equals(username);
//         }
//         return false;
//     }
    
//     // 더미 데이터 생성 메서드
//     private List<Product> createDummyProducts() {
//         List<Product> dummyList = new java.util.ArrayList<>();
        
//         String[] names = {"귀여운 비숑프리제", "ZOOTOPIA 로고", "활발한 코기", "온순한 골든리트리버", 
//                          "아름다운 페르시안 고양이", "활동적인 벵갈 고양이", "노래하는 카나리아", "화려한 앵무새", "열대어 세트"};
//         String[] categories = {"개", "고양이", "새", "물고기", "기타"};
//         String[] images = {"/img/products/BichonFrise.png", "/img/zootopialogo.png", "/img/products/dogcogiactive.png", 
//                           "/img/products/doggoldenritriber.png", "/img/products/catpersian.png", "/img/products/catbangal.png", 
//                           "/img/products/birdcanaria.png", "/img/products/birdperrot.png", "/img/products/fishtropical.png"};
//         int[] prices = {1500000, 25000, 1800000, 2000000, 1200000, 1500000, 150000, 800000, 300000};
        
//         for (int i = 0; i < names.length; i++) {
//             Product product = new Product();
//             product.setNo(i + 1);
//             product.setName(names[i]);
//             product.setCategory(categories[i]);
//             product.setDescription("이것은 " + names[i] + "의 설명입니다.");
//             product.setPrice(prices[i]);
//             product.setImageUrl(images[i]);
//             product.setStatus("판매중");
//             product.setStock(10);
//             product.setViews((i + 1) * 5);
//             product.setLikes((i + 1) * 2);
//             product.setDislikes(i); // 싫어요 수 추가
//             dummyList.add(product);
//         }
        
//         return dummyList;
//     }
// }
