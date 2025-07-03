package com.aloha.zootopia.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import com.aloha.zootopia.domain.Comment;
import com.aloha.zootopia.domain.CustomUser;
import com.aloha.zootopia.domain.Posts;
import com.aloha.zootopia.service.CommentService;
import com.aloha.zootopia.service.PostService;
import com.github.pagehelper.PageInfo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    /**
     * 게시글 목록 (자유글/질문글) + 인기 게시물
     */
    @GetMapping("/list")
    public String list(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "category", required = false) String category,
            Model model
    ) throws Exception {

        // 일반 게시글 목록 가져오기
        PageInfo<Posts> pageInfo = postService.page(page, size, category);
        List<Posts> list = pageInfo.getList();

        // 인기 게시물 목록 가져오기
        List<Posts> topList = postService.getTop10PopularPosts();

        // topList가 null이거나 비어있는 경우 빈 리스트로 처리
        if (topList == null || topList.isEmpty()) {
            System.out.println("🔥 인기글이 없습니다.");
            topList = new ArrayList<>();
        }

        // 모델에 데이터를 추가
        model.addAttribute("pageInfo", pageInfo);  // 페이지네이션 정보
        model.addAttribute("list", list);  // 일반 게시글 목록
        model.addAttribute("category", category);  // 카테고리 필터링 정보
        model.addAttribute("topList", topList);  // 인기 게시물 목록

        return "posts/list";  // 반환할 뷰
    }

    /**
     * 게시글 상세
     */
    @GetMapping("/read/{id}")
    public String read(@PathVariable("id") String id, Model model, HttpServletRequest request) throws Exception {
        Posts post = postService.selectById(id);
        int postId = post.getPostId();

        HttpSession session = request.getSession();
        String viewKey = "viewed_post_" + postId;
        Long lastViewTime = (Long) session.getAttribute(viewKey);

        long now = System.currentTimeMillis();
        long expireTime = 60 * 60 * 1000L;

        if (lastViewTime == null || now - lastViewTime > expireTime) {
            postService.increaseViewCount(postId);
            session.setAttribute(viewKey, now);     
        }

        List<Comment> comments = commentService.getCommentsByPostId(postId);
        post.setComments(comments); 

        model.addAttribute("post", post);
        return "posts/read";
    }

    /**
     * 게시글 작성 폼
     */
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("post", new Posts());
        return "posts/create";
    }

    /**
     * 게시글 작성 처리
     */
    @PostMapping("/create")
    public String create(
            @ModelAttribute Posts post,
            @RequestParam("imageFiles") MultipartFile[] imageFiles,
            @AuthenticationPrincipal CustomUser user,  // 로그인 사용자 정보
            RedirectAttributes ra
    ) throws Exception {
        post.setUserId(user.getUser().getUserId());  // userId 수동 세팅
        boolean result = postService.insert(post, imageFiles);
        if (result) {
            ra.addFlashAttribute("msg", "등록되었습니다.");
            return "redirect:/posts/list";
        }
        ra.addFlashAttribute("error", "등록에 실패했습니다.");
        return "redirect:/posts/create";
    }

    /**
     * 게시글 삭제
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id, RedirectAttributes ra) throws Exception {
        boolean result = postService.deleteById(id);
        if (result) {
            ra.addFlashAttribute("msg", "삭제되었습니다.");
        } else {
            ra.addFlashAttribute("error", "삭제에 실패했습니다.");
        }
        return "redirect:/posts/list";
    }
}

