package com.aloha.zootopia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aloha.zootopia.domain.InsuranceQna;
import com.aloha.zootopia.service.InsuranceQnaService;

@Controller
@RequestMapping("/insurance/qna")
public class InsuranceQnaController {

    @Autowired
    private InsuranceQnaService qnaService;

    // 특정 보험상품 Q&A 목록
    @GetMapping("/{productId}")
    public String list(@PathVariable int productId, Model model) {
        model.addAttribute("qnaList", qnaService.getQnaList(productId));
        model.addAttribute("productId", productId);
        return "insurance/qnaList";  // 예: qnaList.jsp
    }

    // 질문 등록
    @PostMapping("/register")
    @PreAuthorize("hasRole('USER')")
    public String register(InsuranceQna qna, @AuthenticationPrincipal User user) {
        qna.setUserId(Integer.parseInt(user.getUsername()));
        qnaService.registerQuestion(qna);
        return "redirect:/insurance/qna/" + qna.getProductId();
    }

    // AJAX 방식
    @PostMapping("/register-ajax")
    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    public List<InsuranceQna> registerAjax(InsuranceQna qna, @AuthenticationPrincipal User user) {
        qna.setUserId(Integer.parseInt(user.getUsername()));
        qnaService.registerQuestion(qna);
        return qnaService.getQnaList(qna.getProductId());
    }


    // 질문 수정 폼
    @GetMapping("/edit/{qnaId}")
    @PreAuthorize("hasRole('USER')")
    public String showUpdateQuestion(@PathVariable int qnaId,
                                     @AuthenticationPrincipal User user,
                                     Model model) {
        InsuranceQna qna = qnaService.getQna(qnaId);
        if (qna.getUserId() != Integer.parseInt(user.getUsername())) {
            throw new SecurityException("작성자만 수정 가능합니다.");
        }
        model.addAttribute("qna", qna);
        return "insurance/qnaEdit";  // 예: qnaEdit.jsp
    }

    // 질문 수정 처리
    @PostMapping("/edit")
    @PreAuthorize("hasRole('USER')")
    public String updateQuestion(InsuranceQna qna,
                                 @AuthenticationPrincipal User user) {
        InsuranceQna origin = qnaService.getQna(qna.getQnaId());
        if (origin.getUserId() != Integer.parseInt(user.getUsername())) {
            throw new SecurityException("작성자만 수정 가능합니다.");
        }
        qnaService.updateQnaQuestion(qna);
        return "redirect:/insurance/qna/" + qna.getProductId();
    }

    // 답변 등록/수정 (관리자만)
    @PostMapping("/answer")
    @PreAuthorize("hasRole('ADMIN')")
    public String answer(InsuranceQna qna) {
        qnaService.answerQna(qna);
        return "redirect:/insurance/qna/" + qna.getProductId();
    }

    // 질문 삭제
    @PostMapping("/delete/{qnaId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String delete(@PathVariable int qnaId,
                         @RequestParam int productId,
                         @AuthenticationPrincipal User user) {
        InsuranceQna qna = qnaService.getQna(qnaId);
        if (qna.getUserId() != Integer.parseInt(user.getUsername())
                && !user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new SecurityException("작성자 또는 관리자만 삭제 가능합니다.");
        }
        qnaService.deleteQna(qnaId);
        return "redirect:/insurance/qna/" + productId;
    }  
}
