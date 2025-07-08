package com.aloha.zootopia.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import com.aloha.zootopia.domain.Comment;
import com.aloha.zootopia.domain.CustomUser;
import com.aloha.zootopia.domain.LostAnimalPost;
import com.aloha.zootopia.domain.Pagination;
import com.aloha.zootopia.service.LostAnimalCommentService;
import com.aloha.zootopia.service.LostAnimalService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lost")
public class LostAnimalController {

    @Autowired
    private final LostAnimalService lostAnimalService;
    @Autowired
    private final LostAnimalCommentService lostAnimalCommentService;

    // ✅ 목록 페이지
    @GetMapping("/list")
    public String list(@RequestParam(name = "page", defaultValue = "1") int page,
                       Model model) {

        Pagination pagination = new Pagination();
        pagination.setPage(page);
        pagination.setSize(10);
        pagination.setOffset((page - 1) * 10);

        var list = lostAnimalService.getAll(pagination);

        model.addAttribute("list", list);
        model.addAttribute("pageInfo", pagination);
        return "lost/list";
    }

    // ✅ 글쓰기 폼
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("post", new LostAnimalPost());
        return "lost/create";
    }

    // ✅ 글 등록 처리
    @PostMapping("/create")
    public String create(@ModelAttribute LostAnimalPost post,
                         @AuthenticationPrincipal CustomUser user,
                         RedirectAttributes ra) {

        post.setUserId(user.getUser().getUserId());
        boolean result = lostAnimalService.insert(post);

        if (result) {
            ra.addFlashAttribute("msg", "등록되었습니다.");
            return "redirect:/lost/list";
        } else {
            ra.addFlashAttribute("error", "등록 실패");
            return "redirect:/lost/create";
        }
    }

    // ✅ 상세 페이지
    @GetMapping("/read/{id}")
    public String read(@PathVariable("id") int id,
                    Model model,
                    HttpServletRequest request,
                    @AuthenticationPrincipal CustomUser user) {

        LostAnimalPost post = lostAnimalService.getById(id);

        // ✅ 댓글 바인딩
        List<Comment> commentList = lostAnimalCommentService.getCommentsByPostId(id);
        post.setComments(commentList);

        // 조회수 증가 - 세션 중복 방지
        HttpSession session = request.getSession();
        String key = "viewed_lost_" + id;
        Long lastViewed = (Long) session.getAttribute(key);
        long now = System.currentTimeMillis();

        if (lastViewed == null || now - lastViewed > 60 * 60 * 1000) {
            lostAnimalService.increaseViewCount(id);
            session.setAttribute(key, now);
        }

        boolean isOwner = user != null && post.getUserId().equals(user.getUser().getUserId());

        model.addAttribute("post", post);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("loginUserId", user != null ? user.getUser().getUserId() : null); // 댓글 제어용
        return "lost/read";
    }

    // ✅ 수정 폼
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") int id,
                           @AuthenticationPrincipal CustomUser user,
                           Model model,
                           RedirectAttributes ra) {
        LostAnimalPost post = lostAnimalService.getById(id);
        if (!post.getUserId().equals(user.getUser().getUserId())) {
            ra.addFlashAttribute("error", "수정 권한이 없습니다.");
            return "redirect:/lost/list";
        }

        model.addAttribute("post", post);
        return "lost/edit";
    }

    // ✅ 수정 처리
    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute LostAnimalPost post,
                         @AuthenticationPrincipal CustomUser user,
                         RedirectAttributes ra) {

        if (!post.getUserId().equals(user.getUser().getUserId())) {
            ra.addFlashAttribute("error", "수정 권한이 없습니다.");
            return "redirect:/lost/list";
        }

        post.setPostId(id);
        boolean result = lostAnimalService.update(post);

        if (result) {
            ra.addFlashAttribute("msg", "수정 완료");
            return "redirect:/lost/read/" + id;
        } else {
            ra.addFlashAttribute("error", "수정 실패");
            return "redirect:/lost/edit/" + id;
        }
    }

    // ✅ 삭제
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id,
                         @AuthenticationPrincipal CustomUser user,
                         RedirectAttributes ra) {

        LostAnimalPost post = lostAnimalService.getById(id);
        if (!post.getUserId().equals(user.getUser().getUserId())) {
            ra.addFlashAttribute("error", "삭제 권한이 없습니다.");
            return "redirect:/lost/list";
        }

        boolean result = lostAnimalService.delete(id);
        if (result) {
            ra.addFlashAttribute("msg", "삭제되었습니다.");
        } else {
            ra.addFlashAttribute("error", "삭제 실패");
        }
        return "redirect:/lost/list";
    }

    // ✅ 이미지 업로드 (Toast UI 에디터 연동용)
    @PostMapping("/upload/image")
    @ResponseBody
    public Map<String, Object> uploadImage(@RequestParam("image") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            File uploadFolder = new File("C:/upload");
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + "_" + file.getOriginalFilename();
            String savePath = "C:/upload/" + fileName;

            file.transferTo(new File(savePath));

            result.put("success", 1);
            result.put("imageUrl", "/upload/" + fileName);
        } catch (Exception e) {
            result.put("success", 0);
            result.put("message", "업로드 실패");
        }
        return result;
    }
}
