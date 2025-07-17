package com.aloha.zootopia.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
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

    // 등록처리 (관리자)
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public String register(@ModelAttribute InsuranceProduct product,
                        @RequestParam("imageFile") MultipartFile imageFile,
                        RedirectAttributes rttr) {
        try {
            // 1. 유효성 검사
            if (product.getSpecies() == null || product.getSpecies().isBlank()) {
                rttr.addFlashAttribute("errorMessage", "반려동물 종류를 반드시 선택해야 합니다.");
                return "redirect:/insurance/create";
            }

            // 2. 이미지 파일 저장
            if (!imageFile.isEmpty()) {
                // String uploadDir = "src/main/resources/static/uploads/";
                // String originalFilename = imageFile.getOriginalFilename();
                String newFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

                Path uploadPath = Paths.get(uploadDir, newFileName);
                Files.createDirectories(uploadPath.getParent());
                Files.copy(imageFile.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);

                // 3. DB에 저장될 경로 설정
                product.setImagePath("/uploads/" + newFileName);  // InsuranceProduct에 imagePath 필드 필요
            }

            // 4. DB 등록
            productService.registerProduct(product);
            rttr.addFlashAttribute("successMessage", "✅ 보험 상품이 성공적으로 등록되었습니다.");
            return "redirect:/insurance/list";

        } catch (Exception e) {
            rttr.addFlashAttribute("errorMessage", "❌ 파일 업로드 중 오류가 발생했습니다.");
            return "redirect:/insurance/create";
        }
    }

    // 이미지 업로드 전용 (AJAX 호출용)
    @PostMapping("/upload-image")
    @PreAuthorize("hasRole('ADMIN')")
    public String uploadImage(@RequestParam("imageFile") MultipartFile imageFile, Model model) {
        try {
            if (!imageFile.isEmpty()) {
                String newFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir + newFileName);
                Files.createDirectories(uploadPath.getParent());
                Files.copy(imageFile.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);

                String imagePath = "/uploads/" + newFileName;
                model.addAttribute("imagePath", imagePath); // 뷰로 전송
                return "insurance/image-upload-success :: result"; // Thymeleaf fragment로 응답
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "이미지 업로드 실패");
        }
        return "insurance/image-upload-fail :: result";
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
                        @RequestParam("imageFile") MultipartFile imageFile,
                        RedirectAttributes rttr) {
        try {
            if (!imageFile.isEmpty()) {
                String newFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir + newFileName);
                Files.createDirectories(uploadPath.getParent());
                Files.copy(imageFile.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);

                product.setImagePath("/uploads/" + newFileName);
            }

            productService.updateProduct(product);
            rttr.addFlashAttribute("successMessage", "✅ 보험 상품이 성공적으로 수정되었습니다.");
            return "redirect:/insurance/list";

        } catch (Exception e) {
            rttr.addFlashAttribute("errorMessage", "수정 중 오류 발생");
            return "redirect:/insurance/update/" + product.getProductId();
        }
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
