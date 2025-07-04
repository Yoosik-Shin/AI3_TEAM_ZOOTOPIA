package com.aloha.zootopia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aloha.zootopia.domain.InsuranceProduct;
import com.aloha.zootopia.domain.InsuranceQna;
import com.aloha.zootopia.service.InsuranceProductService;
import com.aloha.zootopia.service.InsuranceQnaService;

@Controller
@RequestMapping("/insurance/product")
public class InsuranceProductController {


    @Autowired
    private InsuranceProductService productService;

    @Autowired
    private InsuranceQnaService qnaService;

    // 보험상품 목록
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("products", productService.listProducts());
        return "insurance/list"; // ex) list.jsp
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
    @GetMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public String showRegisterForm() {
        return "insurance/register"; // ex) register.jsp
    }

    // 등록처리 (관리자)
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public String register(InsuranceProduct product) {
        productService.registerProduct(product);
        return "redirect:/insurance/product/list";
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
    public String update(InsuranceProduct product) {
        productService.updateProduct(product);
        return "redirect:/insurance/product/detail/" + product.getProductId();
    }

    // 삭제 (관리자)
    @PostMapping("/delete/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable int productId) {
        productService.deleteProduct(productId);
        return "redirect:/insurance/product/list";
    }
}
