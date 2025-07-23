package com.aloha.zootopia.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aloha.zootopia.domain.InsuranceProduct;
import com.aloha.zootopia.domain.InsuranceQna;
import com.aloha.zootopia.service.InsuranceProductService;
import com.aloha.zootopia.service.InsuranceQnaService;

@Controller
@RequestMapping("/insurance")
public class InsuranceProductController {

    @Value("${file.upload.path}")
    private String uploadDir; 

    @Autowired
    private InsuranceProductService productService;

    @Autowired
    private InsuranceQnaService qnaService;

    // 보험상품 목록
    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int size,
            Model model) {

        int offset = Math.max((page - 1) * size, 0);
        List<InsuranceProduct> products = productService.getProductsPaged(offset, size);
        int totalProducts = productService.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalProducts / size);
        if (totalPages == 0) totalPages = 1;

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "insurance/list";
    }

    // 보험 상세
    @GetMapping("/read/{productId}")
    public String read(@PathVariable int productId, Model model) {
    InsuranceProduct product = productService.getProduct(productId);
    List<InsuranceQna> qnaList = qnaService.getQnaList(productId);

        model.addAttribute("product", product);
        model.addAttribute("qnaList", qnaList);
        return "insurance/read";
    }

    // 등록폼 (관리자)
    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String showRegisterForm() {
        return "insurance/create"; 
    }

    // 보험 상품 등록
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public String register(@ModelAttribute InsuranceProduct product, RedirectAttributes rttr) {
        try {
            if (product.getSpecies() == null || product.getSpecies().isBlank()) {
                rttr.addFlashAttribute("errorMessage", "반려동물 종류를 선택하세요.");
                return "redirect:/insurance/create";
            }
            if (product.getImagePath() == null || product.getImagePath().isBlank()) {
                rttr.addFlashAttribute("errorMessage", "이미지를 먼저 업로드하세요.");
                return "redirect:/insurance/create";
            }

            productService.registerProduct(product);
            rttr.addFlashAttribute("successMessage", "✅ 등록 완료");
            return "redirect:/insurance/list";
        } catch (Exception e) {
            rttr.addFlashAttribute("errorMessage", "❌ 오류 발생: " + e.getMessage());
            return "redirect:/insurance/create";
        }
    }



    // ✅ 이미지 업로드 (AJAX 방식)
    @PostMapping("/upload-image")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public Map<String, String> uploadImageAjax(@RequestParam("imageFile") MultipartFile imageFile) {
        try {
            if (!imageFile.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" +
                    imageFile.getOriginalFilename().replaceAll("[^a-zA-Z0-9.]", "_");

                Path targetPath = Paths.get(uploadDir, fileName);
                Files.createDirectories(targetPath.getParent());
                Files.copy(imageFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                String imagePath = "/upload/" + fileName;
                return Map.of("imagePath", imagePath);  // ✅ success 대신 단일 반환
            } else {
                return Map.of("message", "이미지 파일이 비어 있습니다.");
            }
        } catch (IOException e) {
            return Map.of("message", "업로드 실패: " + e.getMessage());
        }
    }


    // 수정폼 (관리자)
    @GetMapping("/update/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showUpdateForm(@PathVariable int productId, Model model) {
        model.addAttribute("product", productService.getProduct(productId));
        return "insurance/update"; // ex) update.jsp
    }

    // 수정처리 (관리자)
    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@ModelAttribute InsuranceProduct product,
                         RedirectAttributes rttr) {
        try {
            productService.updateProduct(product);
            rttr.addFlashAttribute("successMessage", "✅ 수정 완료");
        } catch (Exception e) {
            rttr.addFlashAttribute("errorMessage", "❌ 수정 실패: " + e.getMessage());
        }
        return "redirect:/insurance/list";
    }

    // 삭제 (관리자)
    @PostMapping("/delete/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable int productId, RedirectAttributes rttr) {
        try {
            productService.deleteProduct(productId);
            rttr.addFlashAttribute("successMessage", "✅ 보험 상품이 삭제되었습니다.");
        } catch (Exception e) {
            rttr.addFlashAttribute("errorMessage", "❌ 삭제 중 오류가 발생했습니다.");
        }
        return "redirect:/insurance/list";
    }
}
