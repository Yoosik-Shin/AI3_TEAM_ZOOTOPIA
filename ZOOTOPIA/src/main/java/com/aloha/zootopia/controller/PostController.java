package com.aloha.zootopia.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

import com.aloha.zootopia.domain.CustomUser;
import com.aloha.zootopia.domain.Posts;
import com.aloha.zootopia.service.PostService;
import com.github.pagehelper.PageInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

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
        PageInfo<Posts> pageInfo = postService.page(page, size);
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
<<<<<<< HEAD
    public String read(
        @PathVariable("id") String id,
        Model model,
        HttpServletRequest request,
        @AuthenticationPrincipal CustomUser user  
    ) throws Exception {
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

        boolean isOwner = user != null && post.getUserId().equals(user.getUser().getUserId());

    
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        post.setComments(comments);
        

=======
    public String read(@PathVariable("id") String id, Model model) throws Exception {
        Posts post = postService.selectById(id);

        postService.increaseViewCount(post.getPostId());

        if (post.getComments() == null) {
            post.setComments(new ArrayList<>());
        }

>>>>>>> main
        model.addAttribute("post", post);
        model.addAttribute("isOwner", isOwner);

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
            @AuthenticationPrincipal CustomUser user,  // 로그인 사용자 정보
            RedirectAttributes ra
    ) throws Exception {

        if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
            ra.addFlashAttribute("error", "제목은 1자 이상 입력해주세요.");
            return "redirect:/posts/create";
        }

        if (post.getContent() == null || post.getContent().trim().length() < 5) {
            ra.addFlashAttribute("error", "본문은 5자 이상 입력해주세요.");
            return "redirect:/posts/create";
        }


        post.setUserId(user.getUser().getUserId());  // userId 수동 세팅
        
        boolean result = postService.insert(post);
        if (result) {
            ra.addFlashAttribute("msg", "등록되었습니다.");
            return "redirect:/posts/list";
        }
        ra.addFlashAttribute("error", "등록에 실패했습니다.");
        return "redirect:/posts/create";
    }

    @PostMapping("/upload/image")
    @ResponseBody
    public Map<String, Object> uploadImage(@RequestParam("image") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + "_" + file.getOriginalFilename();
            String savePath = "C:/upload/" + fileName;

            file.transferTo(new File(savePath));

            result.put("success", 1);
            result.put("imageUrl", "/upload/" + fileName); // 👈 에디터에 삽입될 이미지 URL
        } catch (Exception e) {
            result.put("success", 0);
            result.put("message", "업로드 실패");
        }
        return result;
    }







    /**
     * 게시글 삭제
     */
    @PostMapping("/delete/{id}")
    public String delete(
        @PathVariable("id") String id,
        @AuthenticationPrincipal CustomUser user,   // 🔐 로그인 정보 가져오기
        RedirectAttributes ra
    ) throws Exception {
        // 🛡️ 글쓴이인지 확인
        boolean isOwner = postService.isOwner(id, user.getUser().getUserId());

        if (!isOwner) {
            ra.addFlashAttribute("error", "삭제 권한이 없습니다.");
            return "redirect:/posts/list";
        }

        boolean result = postService.deleteById(id);

        if (result) {
            ra.addFlashAttribute("msg", "삭제되었습니다.");
        } else {
            ra.addFlashAttribute("error", "삭제에 실패했습니다.");
        }

        return "redirect:/posts/list";
    }


    @GetMapping("/edit/{id}")
    public String editForm(
        @PathVariable("id") String id,
        @AuthenticationPrincipal CustomUser user,
        Model model,
        RedirectAttributes ra
    ) throws Exception {

        // 🔐 글쓴이 검증
        boolean isOwner = postService.isOwner(id, user.getUserId());
        if (!isOwner) {
            ra.addFlashAttribute("error", "수정 권한이 없습니다.");
            return "redirect:/posts/list";
        }

        // ✅ 통과: 수정 폼에 데이터 전달
        Posts post = postService.selectById(id);
        model.addAttribute("post", post);
        return "posts/edit";  // edit.html로 이동
    }


    @PostMapping("/edit/{id}")
    public String update(
        @PathVariable("id") String id,
        @ModelAttribute Posts post,
        @RequestParam("imageFiles") MultipartFile[] imageFiles,
        @AuthenticationPrincipal CustomUser user,
        RedirectAttributes ra
    ) throws Exception {

        if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
            ra.addFlashAttribute("error", "제목은 1자 이상 입력해주세요.");
            return "redirect:/posts/edit/" + post.getId();
        }

        if (post.getContent() == null || post.getContent().trim().length() < 5) {
            ra.addFlashAttribute("error", "본문은 5자 이상 입력해주세요.");
            return "redirect:/posts/edit/" + post.getId();
        }


        // 🔒 글쓴이 확인
        if (!postService.isOwner(id, user.getUserId())) {
            ra.addFlashAttribute("error", "수정 권한이 없습니다.");
            return "redirect:/posts/list";
        }

        // postId는 path variable로 받았지만, @ModelAttribute에 자동 매핑 안 될 수 있으므로 수동 설정
        post.setId(id);
        post.setUserId(user.getUserId());

        boolean result = postService.updateById(post); // 이미지 업데이트 제외

        if (result) {
            ra.addFlashAttribute("msg", "글이 수정되었습니다.");
            return "redirect:/posts/read/" + id;
        } else {
            ra.addFlashAttribute("error", "글 수정에 실패했습니다.");
            return "redirect:/posts/edit/" + id;
        }
    }



}