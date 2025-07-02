package com.aloha.zootopia.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aloha.zootopia.domain.Pagination;
import com.aloha.zootopia.domain.PostImage;
import com.aloha.zootopia.domain.Posts;
import com.aloha.zootopia.mapper.PostImageMapper;
import com.aloha.zootopia.mapper.PostMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    @Autowired private PostMapper postMapper;
    @Autowired private PostImageMapper postImageMapper;

    private final String uploadDir = "C:/upload"; // 로컬 저장경로

    @Override
    public List<Posts> list() throws Exception {
        return postMapper.list();
    }

    @Override
    public List<Posts> page(Pagination pagination) throws Exception {
        long total = postMapper.count(pagination);
        pagination.setTotal(total);
        return postMapper.page(pagination);
    }

    @Override
    public PageInfo<Posts> page(int page, int size) throws Exception {
        PageHelper.startPage(page, size);
        List<Posts> list = postMapper.list();
        return new PageInfo<>(list, 10);
    }

    @Override
    public List<Posts> getTopN(int limit) throws Exception {
        return postMapper.selectTopNByLikeCount(limit);
    }

    @Override
    public Posts selectById(String id) throws Exception {
        return postMapper.selectById(id);
    }

    @Override
    public boolean insert(Posts post, MultipartFile[] imageFiles) throws Exception {
        // 게시글 DB 등록
        boolean success = postMapper.insert(post) > 0;
        if (!success) return false;

        List<PostImage> imageList = new ArrayList<>();
        int ordering = 1;

        for (MultipartFile file : imageFiles) {
            if (file != null && !file.isEmpty()) {
                String originalName = file.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                String savedName = uuid + "_" + originalName;
                String savePath = uploadDir + File.separator + savedName;

                // 실제 파일 저장
                file.transferTo(new File(savePath));

                // 썸네일용 첫 이미지 URL 설정
                if (ordering == 1) {
                    post.setThumbnailUrl("/upload/" + savedName);
                    postMapper.updateById(post); // 썸네일 경로 업데이트
                }

                // 이미지 객체 구성
                PostImage image = PostImage.builder()
                    .postId(post.getPostId())
                    .imageUrl("/upload/" + savedName)
                    .ordering(ordering++)
                    .build();

                imageList.add(image);
            }
        }

        // 이미지 목록 insert
        for (PostImage img : imageList) {
            postImageMapper.insert(img);
        }

        return true;
    }

    @Override
    public boolean updateById(Posts post) throws Exception {
        return postMapper.updateById(post) > 0;
    }

    @Override
    public boolean deleteById(String id) throws Exception {
        return postMapper.deleteById(id) > 0;
    }

    @Override
    public boolean isOwner(String id, Integer userId) throws Exception {
        Posts post = postMapper.selectById(id);
        return post != null && post.getUserId().equals(userId);
    }

    @Override
    public void increaseViewCount(Integer postId) {
        postMapper.updateViewCount(postId);
    }

    @Override
    public List<Posts> getTop10PopularPosts() {
        return postMapper.selectTop10ByPopularity();
    }

}
