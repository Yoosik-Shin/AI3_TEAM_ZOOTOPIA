package com.aloha.zootopia.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.aloha.zootopia.domain.Product;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    // 더미 상품 생성 메소드 (ProductController에서 복사)
    private List<Product> createDummyProducts() {
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Product p = new Product();
            p.setNo(i);
            p.setName("ZOOTOPIA 프리미엄 사료 " + i);
            p.setCategory(i % 2 == 0 ? "사료" : "용품");
            p.setDescription("반려동물을 위한 최고급 " + (i % 2 == 0 ? "사료" : "용품") + "입니다.");
            p.setPrice(15000 + i * 1000);
            p.setImageUrl("/img/default-thumbnail.png");
            products.add(p);
        }
        return products;
    }
    
    // 장바구니 페이지
    @GetMapping("")
    public String cart(HttpSession session, Model model) {
        System.out.println("=== CartController /cart 호출됨 ===");
        
        try {
            // 세션에서 장바구니 정보 조회
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cartItems = (List<Map<String, Object>>) session.getAttribute("cartItems");
            
            if (cartItems == null) {
                cartItems = new ArrayList<>();
                session.setAttribute("cartItems", cartItems);
            }
            
            // 장바구니 총액 계산
            int totalAmount = 0;
            int totalQuantity = 0;
            
            for (Map<String, Object> item : cartItems) {
                int price = (Integer) item.get("price");
                int quantity = (Integer) item.get("quantity");
                totalAmount += price * quantity;
                totalQuantity += quantity;
            }
            
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("totalQuantity", totalQuantity);
            
            return "cart/cart";
            
        } catch (Exception e) {
            System.err.println("장바구니 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "장바구니 정보를 불러오는 중 오류가 발생했습니다.");
            return "error/500";
        }
    }
    
    // 장바구니에 상품 추가
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(@RequestParam int productNo,
                                                        @RequestParam int quantity,
                                                        HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 상품 정보 조회
            List<Product> allProducts = createDummyProducts();
            Product product = allProducts.stream()
                .filter(p -> p.getNo() == productNo)
                .findFirst()
                .orElse(null);
            
            if (product == null) {
                response.put("success", false);
                response.put("message", "상품을 찾을 수 없습니다.");
                return ResponseEntity.ok(response);
            }
            
            // 세션에서 장바구니 정보 조회
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cartItems = (List<Map<String, Object>>) session.getAttribute("cartItems");
            
            if (cartItems == null) {
                cartItems = new ArrayList<>();
                session.setAttribute("cartItems", cartItems);
            }
            
            // 이미 장바구니에 있는 상품인지 확인
            boolean found = false;
            for (Map<String, Object> item : cartItems) {
                if ((Integer) item.get("productNo") == productNo) {
                    // 수량 증가
                    int currentQuantity = (Integer) item.get("quantity");
                    item.put("quantity", currentQuantity + quantity);
                    found = true;
                    break;
                }
            }
            
            // 새로운 상품이면 추가
            if (!found) {
                Map<String, Object> newItem = new HashMap<>();
                newItem.put("productNo", product.getNo());
                newItem.put("name", product.getName());
                newItem.put("price", product.getPrice());
                newItem.put("quantity", quantity);
                newItem.put("imageUrl", product.getImageUrl());
                newItem.put("category", product.getCategory());
                cartItems.add(newItem);
            }
            
            response.put("success", true);
            response.put("message", "장바구니에 추가되었습니다.");
            response.put("cartCount", cartItems.size());
            
        } catch (Exception e) {
            System.err.println("장바구니 추가 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "장바구니 추가 중 오류가 발생했습니다.");
        }
        
        return ResponseEntity.ok(response);
    }
    
    // 장바구니에 상품 추가 (GET 방식 - 상품 상세 페이지에서 호출)
    @GetMapping("/add")
    public String addToCartGet(@RequestParam int productNo,
                              @RequestParam int quantity,
                              @RequestParam(required = false, defaultValue = "false") boolean direct,
                              HttpSession session) {
        System.out.println("=== CartController /cart/add (GET) 호출됨 ===");
        System.out.println("상품번호: " + productNo + ", 수량: " + quantity + ", 바로구매: " + direct);
        
        try {
            // ProductController에서 상품 정보 조회
            Product product = createDummyProducts().stream()
                .filter(p -> p.getNo() == productNo)
                .findFirst()
                .orElse(null);
            
            if (product == null) {
                return "redirect:/products/detail/" + productNo + "?error=product_not_found";
            }
            
            // 세션에서 장바구니 정보 조회
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cartItems = (List<Map<String, Object>>) session.getAttribute("cartItems");
            
            if (cartItems == null) {
                cartItems = new ArrayList<>();
                session.setAttribute("cartItems", cartItems);
            }
            
            // 이미 장바구니에 있는 상품인지 확인
            boolean found = false;
            for (Map<String, Object> item : cartItems) {
                if ((Integer) item.get("productNo") == productNo) {
                    // 수량 증가
                    int currentQuantity = (Integer) item.get("quantity");
                    item.put("quantity", currentQuantity + quantity);
                    found = true;
                    break;
                }
            }
            
            // 새로운 상품이면 추가
            if (!found) {
                Map<String, Object> newItem = new HashMap<>();
                newItem.put("productNo", product.getNo());
                newItem.put("name", product.getName());
                newItem.put("price", product.getPrice());
                newItem.put("quantity", quantity);
                newItem.put("imageUrl", product.getImageUrl());
                newItem.put("category", product.getCategory());
                cartItems.add(newItem);
            }
            
            // 바로 구매인 경우 결제 페이지로, 아니면 장바구니 페이지로
            if (direct) {
                return "redirect:/cart/checkout";
            } else {
                return "redirect:/cart?added=true";
            }
            
        } catch (Exception e) {
            System.err.println("장바구니 추가 중 오류: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/products/detail/" + productNo + "?error=cart_add_failed";
        }
    }
    
    // 장바구니에 상품 추가 (폼 전송용)
    @PostMapping("/add-form")
    public String addToCartForm(@RequestParam int productNo,
                                @RequestParam int quantity,
                                HttpSession session) {
        try {
            // 상품 정보 조회
            List<Product> allProducts = createDummyProducts();
            Product product = allProducts.stream()
                .filter(p -> p.getNo() == productNo)
                .findFirst()
                .orElse(null);
            if (product == null) {
                return "redirect:/products/detail/" + productNo + "?error=notfound";
            }
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cartItems = (List<Map<String, Object>>) session.getAttribute("cartItems");
            if (cartItems == null) {
                cartItems = new ArrayList<>();
                session.setAttribute("cartItems", cartItems);
            }
            boolean found = false;
            for (Map<String, Object> item : cartItems) {
                if ((Integer) item.get("productNo") == productNo) {
                    int currentQuantity = (Integer) item.get("quantity");
                    item.put("quantity", currentQuantity + quantity);
                    found = true;
                    break;
                }
            }
            if (!found) {
                Map<String, Object> newItem = new HashMap<>();
                newItem.put("productNo", product.getNo());
                newItem.put("name", product.getName());
                newItem.put("price", product.getPrice());
                newItem.put("quantity", quantity);
                newItem.put("imageUrl", product.getImageUrl());
                newItem.put("category", product.getCategory());
                cartItems.add(newItem);
            }
            return "redirect:/cart";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/products/detail/" + productNo + "?error=exception";
        }
    }
    
    // 장바구니 상품 수량 변경
    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateQuantity(@RequestParam int productNo,
                                                             @RequestParam int quantity,
                                                             HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cartItems = (List<Map<String, Object>>) session.getAttribute("cartItems");
            
            if (cartItems != null) {
                for (Map<String, Object> item : cartItems) {
                    if ((Integer) item.get("productNo") == productNo) {
                        if (quantity <= 0) {
                            cartItems.remove(item);
                        } else {
                            item.put("quantity", quantity);
                        }
                        break;
                    }
                }
            }
            
            response.put("success", true);
            response.put("message", "수량이 변경되었습니다.");
            
        } catch (Exception e) {
            System.err.println("장바구니 수량 변경 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "수량 변경 중 오류가 발생했습니다.");
        }
        
        return ResponseEntity.ok(response);
    }
    
    // 장바구니 상품 삭제
    @PostMapping("/remove")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeFromCart(@RequestParam int productNo,
                                                             HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cartItems = (List<Map<String, Object>>) session.getAttribute("cartItems");
            
            if (cartItems != null) {
                cartItems.removeIf(item -> (Integer) item.get("productNo") == productNo);
            }
            
            response.put("success", true);
            response.put("message", "상품이 삭제되었습니다.");
            
        } catch (Exception e) {
            System.err.println("장바구니 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "상품 삭제 중 오류가 발생했습니다.");
        }
        
        return ResponseEntity.ok(response);
    }
    
    // 결제 페이지로 이동
    @PostMapping("/checkout")
    public String checkout(@RequestParam(required = false) String productNos,
                          @RequestParam(required = false) String quantities,
                          HttpSession session,
                          Model model) {
        try {
            List<Map<String, Object>> checkoutItems = new ArrayList<>();
            
            if (productNos != null && quantities != null) {
                // 직접 구매의 경우
                String[] productNoArray = productNos.split(",");
                String[] quantityArray = quantities.split(",");
                
                List<Product> allProducts = createDummyProducts();
                
                for (int i = 0; i < productNoArray.length; i++) {
                    int productNo = Integer.parseInt(productNoArray[i]);
                    int quantity = Integer.parseInt(quantityArray[i]);
                    
                    Product product = allProducts.stream()
                        .filter(p -> p.getNo() == productNo)
                        .findFirst()
                        .orElse(null);
                    
                    if (product != null) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("productNo", product.getNo());
                        item.put("name", product.getName());
                        item.put("price", product.getPrice());
                        item.put("quantity", quantity);
                        item.put("imageUrl", product.getImageUrl());
                        checkoutItems.add(item);
                    }
                }
            } else {
                // 장바구니에서 구매의 경우
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> cartItems = (List<Map<String, Object>>) session.getAttribute("cartItems");
                if (cartItems != null) {
                    checkoutItems.addAll(cartItems);
                }
            }
            
            // 총액 계산
            int totalAmount = 0;
            for (Map<String, Object> item : checkoutItems) {
                int price = (Integer) item.get("price");
                int quantity = (Integer) item.get("quantity");
                totalAmount += price * quantity;
            }
            
            model.addAttribute("checkoutItems", checkoutItems);
            model.addAttribute("totalAmount", totalAmount);
            
            return "cart/checkout";
            
        } catch (Exception e) {
            System.err.println("결제 페이지 이동 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "결제 페이지로 이동하는 중 오류가 발생했습니다.");
            return "error/500";
        }
    }
}
