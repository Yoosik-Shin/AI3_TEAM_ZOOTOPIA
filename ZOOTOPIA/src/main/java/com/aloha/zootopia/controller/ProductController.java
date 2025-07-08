package com.aloha.zootopia.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
        return "redirect:/products/listp";
    }
    
    // 동적 기능이 포함된 상품 목록 페이지 (Bootstrap + Thymeleaf)
    @GetMapping("/listp")
    public String list(@RequestParam(value = "category", required = false) String category,
                      @RequestParam(value = "search", required = false) String search,
                      @RequestParam(value = "page", defaultValue = "1") int page,
                      @RequestParam(value = "size", defaultValue = "9") int size,
                      Model model) {
        System.out.println("=== ProductController /listp 호출됨 ===");
        System.out.println("카테고리: " + category + ", 검색어: " + search + ", 페이지: " + page);
        
        try {
            // 더미 데이터 생성
            List<Product> allProducts = createDummyProducts();
            
            // 카테고리 필터링
            if (category != null && !category.isEmpty() && !"전체".equals(category)) {
                allProducts = allProducts.stream()
                    .filter(p -> category.equals(p.getCategory()))
                    .collect(java.util.stream.Collectors.toList());
            }
            
            // 검색 필터링
            if (search != null && !search.isEmpty()) {
                allProducts = allProducts.stream()
                    .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()) ||
                               p.getDescription().toLowerCase().contains(search.toLowerCase()))
                    .collect(java.util.stream.Collectors.toList());
            }
            
            // 페이지네이션 계산
            int totalProducts = allProducts.size();
            int startIndex = (page - 1) * size;
            int endIndex = Math.min(startIndex + size, totalProducts);
            
            List<Product> products = allProducts.subList(startIndex, endIndex);
            
            // 페이지네이션 정보 생성
            Pagination pagination = new Pagination(page, totalProducts);
            pagination.setSize(size);
            pagination.setCategory(category);
            
            // 카테고리 목록 (실제 더미 데이터에 있는 카테고리로 수정)
            List<String> categories = java.util.Arrays.asList("전체", "사료", "용품");
            
            // 모델에 데이터 추가
            model.addAttribute("products", products);
            model.addAttribute("pagination", pagination);
            model.addAttribute("categories", categories);
            model.addAttribute("currentCategory", category != null ? category : "전체");
            model.addAttribute("currentSearch", search != null ? search : "");
            model.addAttribute("totalProducts", totalProducts);
            
            return "products/listp_dynamic";
            
        } catch (Exception e) {
            System.err.println("상품 목록 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "상품 목록을 불러오는 중 오류가 발생했습니다.");
            return "products/listp_minimal2";
        }
    }
    
    // 상품 상세 페이지
    @GetMapping("/detail/{no}")
    public String detail(@PathVariable int no, Model model) {
        System.out.println("=== ProductController /detail/" + no + " 호출됨 ===");
        
        try {
            // 더미 데이터에서 해당 상품 찾기
            List<Product> allProducts = createDummyProducts();
            System.out.println("총 더미 상품 수: " + allProducts.size());
            
            Product product = null;
            for (Product p : allProducts) {
                if (p.getNo() == no) {
                    product = p;
                    break;
                }
            }
            
            if (product == null) {
                System.out.println("상품 번호 " + no + "을 찾을 수 없음");
                model.addAttribute("error", "상품을 찾을 수 없습니다.");
                return "error/404";
            }
            
            System.out.println("찾은 상품: " + product.getName() + ", 가격: " + product.getPrice());
            
            // 관련 상품들 (같은 카테고리 상품 3개) - 더 안전한 방식으로
            List<Product> relatedProducts = new java.util.ArrayList<>();
            for (Product p : allProducts) {
                if (p.getCategory().equals(product.getCategory()) && p.getNo() != no) {
                    relatedProducts.add(p);
                    if (relatedProducts.size() >= 3) break;
                }
            }
            
            System.out.println("관련 상품 수: " + relatedProducts.size());
            
            model.addAttribute("product", product);
            model.addAttribute("relatedProducts", relatedProducts);
            
            return "products/detail";
            
        } catch (Exception e) {
            System.err.println("상품 상세 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "상품 정보를 불러오는 중 오류가 발생했습니다.");
            return "error/500";
        }
    }
    
    // 테스트용 상품 상세 페이지
    @GetMapping("/detail-test/{no}")
    public String detailTest(@PathVariable int no, Model model) {
        System.out.println("=== 테스트용 상품 상세 페이지 호출됨: " + no + " ===");
        return "products/detail_test";
    }
    
    // 정적 테스트 페이지
    @GetMapping("/detail-static/{no}")
    public String detailStatic(@PathVariable int no, Model model) {
        System.out.println("=== 정적 테스트 페이지 호출됨: " + no + " ===");
        return "products/detail_static";
    }
    
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
    
//     // 장바구니 추가 기능
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
    
    // 더미 데이터 생성 메서드 - 실제 상품 이미지 기반 (CartController에서도 사용)
    public List<Product> createDummyProducts() {
        List<Product> dummyList = new java.util.ArrayList<>();
        
        // 사료 상품들
        addProduct(dummyList, 1, "새 사료 - 사랑에 빠진 새", "사료", "새들이 좋아하는 영양가 높은 사료입니다. 건강한 털과 활발한 활동을 도와줍니다.", 25000, "/assets/dist/img/products/foodbirdfallinlove.png");
        addProduct(dummyList, 2, "새 사료 - 부엉이가 본", "사료", "다양한 곡물과 씨앗이 포함된 프리미엄 새 사료입니다.", 28000, "/assets/dist/img/products/foodbirdowlsee.png");
        addProduct(dummyList, 3, "새 사료 - 스크림", "사료", "새들의 건강을 위한 특별한 배합의 사료입니다.", 22000, "/assets/dist/img/products/foodbirdscream.png");
        addProduct(dummyList, 4, "고양이 사료 - 피프티", "사료", "고양이의 영양 균형을 고려한 프리미엄 사료입니다.", 35000, "/assets/dist/img/products/foodcatfifty.png");
        addProduct(dummyList, 5, "고양이 사료 - 생선 맛", "사료", "신선한 생선을 주원료로 한 고양이 사료입니다.", 32000, "/assets/dist/img/products/foodcatfishtaste.png");
        addProduct(dummyList, 6, "고양이 사료 - 고뜨", "사료", "고양이가 좋아하는 맛과 영양을 동시에 충족하는 사료입니다.", 38000, "/assets/dist/img/products/foodcatgoddu.png");
        addProduct(dummyList, 7, "개 사료 - 아빠가 좋아해", "사료", "개들이 좋아하는 맛있는 사료입니다. 영양가가 풍부합니다.", 42000, "/assets/dist/img/products/foodddogaddylovesit.png");
        addProduct(dummyList, 8, "개&고양이 건조 사료", "사료", "개와 고양이 모두 먹을 수 있는 건조 사료입니다.", 45000, "/assets/dist/img/products/fooddogandcatdried.png");
        addProduct(dummyList, 9, "개&고양이 습식 사료", "사료", "수분이 풍부한 습식 사료로 기호성이 뛰어납니다.", 48000, "/assets/dist/img/products/fooddogcatmoistured.png");
        addProduct(dummyList, 10, "개 사료 - 껌1", "사료", "개의 치아 건강을 위한 껌 형태의 사료입니다.", 18000, "/assets/dist/img/products/fooddoggum1.png");
        addProduct(dummyList, 11, "개 사료 - 하트빔", "사료", "하트 모양의 귀여운 개 사료입니다.", 25000, "/assets/dist/img/products/fooddogheartbeam.png");
        addProduct(dummyList, 12, "개 사료 - 고기", "사료", "신선한 고기를 주원료로 한 프리미엄 개 사료입니다.", 55000, "/assets/dist/img/products/fooddogmeat.png");
        
        // 용품 상품들
        addProduct(dummyList, 13, "고양이 목걸이", "용품", "고양이용 방울이 달린 예쁜 목걸이입니다.", 15000, "/assets/dist/img/products/productcatbellnecklace.png");
        addProduct(dummyList, 14, "고양이 식기", "용품", "고양이 전용 식기입니다. 먹기 편한 높이와 각도로 설계되었습니다.", 18000, "/assets/dist/img/products/productcatbowl.png");
        addProduct(dummyList, 15, "고양이 위생패드", "용품", "고양이가 편안하게 쉴 수 있는 위생패드입니다.", 25000, "/assets/dist/img/products/productcathygienepad.png");
        addProduct(dummyList, 16, "고양이 물그릇", "용품", "고양이 전용 물그릇입니다. 물이 흘러넘치지 않도록 설계되었습니다.", 12000, "/assets/dist/img/products/productcatwaterbowl.png");
        addProduct(dummyList, 17, "개 식기", "용품", "개 전용 식기입니다. 크기별로 다양하게 준비되어 있습니다.", 20000, "/assets/dist/img/products/productdogbowl.png");
        addProduct(dummyList, 18, "개 하네스", "용품", "산책 시 사용하는 개 하네스입니다. 편안하고 안전합니다.", 35000, "/assets/dist/img/products/productdogharness.png");
        addProduct(dummyList, 19, "개 위생패드", "용품", "개가 편안하게 쉴 수 있는 위생패드입니다.", 28000, "/assets/dist/img/products/productdoghygienepad.png");
        addProduct(dummyList, 20, "개 물그릇", "용품", "개 전용 물그릇입니다. 넘어지지 않도록 바닥에 미끄럼 방지 처리가 되어 있습니다.", 15000, "/assets/dist/img/products/productdogwaterbowl.png");
        addProduct(dummyList, 21, "위생 플라스틱 봉투", "용품", "산책 시 사용하는 배변봉투입니다. 친환경 소재로 만들어졌습니다.", 8000, "/assets/dist/img/products/producthygieneplasticbag.png");
        addProduct(dummyList, 22, "위생 화장실", "용품", "반려동물 전용 화장실입니다. 냄새 차단과 청소가 쉬운 디자인으로 제작되었습니다.", 45000, "/assets/dist/img/products/producthygienetoilet.png");
        addProduct(dummyList, 23, "펫 침대", "용품", "반려동물이 편안하게 잠들 수 있는 침대입니다.", 65000, "/assets/dist/img/products/productpetbed.png");
        addProduct(dummyList, 24, "펫 케이지", "용품", "반려동물용 케이지입니다. 안전하고 통풍이 잘 됩니다.", 120000, "/assets/dist/img/products/productpetcage.png");
        addProduct(dummyList, 25, "펫 캐리어", "용품", "반려동물과 함께 외출할 때 사용하는 캐리어입니다.", 85000, "/assets/dist/img/products/productpetcarriage.png");
        addProduct(dummyList, 26, "펫 빗", "용품", "반려동물의 털을 정리하는 빗입니다.", 22000, "/assets/dist/img/products/productpetcomb.png");
        addProduct(dummyList, 27, "펫 쿠션", "용품", "반려동물이 편안하게 쉴 수 있는 쿠션입니다.", 35000, "/assets/dist/img/products/productpetcousion.png");
        addProduct(dummyList, 28, "펫 발톱깎이", "용품", "반려동물의 발톱을 안전하게 깎을 수 있는 도구입니다.", 15000, "/assets/dist/img/products/productpetcutter.png");
        addProduct(dummyList, 29, "펫 귀 청소용품", "용품", "반려동물의 귀를 깨끗하게 청소하는 용품입니다.", 18000, "/assets/dist/img/products/productpetearcleaner.png");
        addProduct(dummyList, 30, "펫 하우스", "용품", "반려동물을 위한 집입니다. 실내외 모두 사용 가능합니다.", 150000, "/assets/dist/img/products/productpethouse.png");
        addProduct(dummyList, 31, "펫 목걸이", "용품", "반려동물용 목걸이입니다. 이름표를 달 수 있습니다.", 25000, "/assets/dist/img/products/productpetnecklace.png");
        addProduct(dummyList, 32, "펫 샴푸", "용품", "반려동물 전용 샴푸입니다. 피부에 자극이 적습니다.", 28000, "/assets/dist/img/products/productpetshampoo.png");
        
        return dummyList;
    }
    
    // 상품 추가 헬퍼 메서드
    private void addProduct(List<Product> list, int no, String name, String category, String description, int price, String imageUrl) {
        Product product = new Product();
        product.setNo(no);
        product.setName(name);
        product.setCategory(category);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageUrl(imageUrl);
        product.setStatus("판매중");
        product.setStock((int)(Math.random() * 20) + 5); // 5-24개 랜덤 재고
        product.setViews((int)(Math.random() * 100) + 1); // 1-100 랜덤 조회수
        product.setLikes((int)(Math.random() * 20)); // 0-19 랜덤 좋아요
        product.setDislikes((int)(Math.random() * 5)); // 0-4 랜덤 싫어요
        list.add(product);
    }
    
    // 더미 데이터 테스트용 엔드포인트
    @GetMapping("/test-dummy")
    @ResponseBody
    public String testDummy() {
        try {
            List<Product> products = createDummyProducts();
            StringBuilder sb = new StringBuilder();
            sb.append("총 상품 수: ").append(products.size()).append("<br>");
            
            for (Product p : products) {
                if (p.getNo() == 7) {
                    sb.append("상품 7번: ").append(p.getName())
                      .append(", 가격: ").append(p.getPrice())
                      .append(", 카테고리: ").append(p.getCategory())
                      .append(", 이미지: ").append(p.getImageUrl())
                      .append("<br>");
                    break;
                }
            }
            
            return sb.toString();
        } catch (Exception e) {
            return "오류: " + e.getMessage() + "<br>" + java.util.Arrays.toString(e.getStackTrace());
        }
    }
}
