package com.aloha.zootopia.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.zootopia.domain.Pagination;
import com.aloha.zootopia.domain.Posts;
import com.aloha.zootopia.domain.Tag;
import com.aloha.zootopia.mapper.PostMapper;
import com.aloha.zootopia.mapper.TagMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    @Autowired private PostMapper postMapper;
    
    @Autowired private TagMapper tagMapper;


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
    public PageInfo<Posts> page(int page, int size, String category) {
        PageHelper.startPage(page, size);
        List<Posts> postList = postMapper.pageByCategory(category);
        PageInfo<Posts> pageInfo = new PageInfo<>(postList);

        if (!postList.isEmpty()) {
            List<Integer> postIds = postList.stream()
                    .map(Posts::getPostId)
                    .toList();

            List<Tag> tagResults = postMapper.selectTagsByPostIds(postIds);

            Map<Integer, List<Tag>> tagMap = tagResults.stream()
                    .collect(Collectors.groupingBy(Tag::getPostId));

            for (Posts post : postList) {
                List<Tag> tagList = tagMap.get(post.getPostId());
                if (tagList != null) {
                    post.setTagList(tagList);
                }
            }
        }

        return pageInfo;
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
    public boolean insert(Posts post) throws Exception {
        // 게시글 저장
        boolean success = postMapper.insert(post) > 0;
        if (!success) return false;

        // ✅ 썸네일 자동 추출: 본문(content)에서 첫 <img src="..."> 태그 파싱
        String content = post.getContent();
        if (content != null) {
            // 정규표현식으로 첫 번째 이미지 src 추출
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("<img[^>]+src=[\"']?([^\"'>]+)[\"']?").matcher(content);
            if (matcher.find()) {
                String firstImgSrc = matcher.group(1); // ex) /upload/abc.jpg
                post.setThumbnailUrl(firstImgSrc);
                postMapper.updateThumbnail(post); // 썸네일 반영
            }
        }

        // ✅ 태그 처리 (기존 유지)
        String tagStr = post.getTags();  // 예: "고양이,귀여움,햇살"
        if (tagStr != null && !tagStr.trim().isEmpty()) {
            String[] tagNames = tagStr.split(",");
            for (String rawName : tagNames) {
                String name = rawName.trim();
                if (name.isEmpty()) continue;

                Integer tagId = tagMapper.findTagIdByName(name);
                if (tagId == null) {
                    Tag tag = new Tag();
                    tag.setName(name);
                    tagMapper.insertTag(tag);
                    tagId = tag.getTagId();  // generated key 반환됨
                }

                tagMapper.insertPostTag(post.getPostId(), tagId);
            }
        }

        return true;
    }



    @Override
    public boolean updateById(Posts post) throws Exception {
        // 1. 게시글 본문, 제목, 카테고리 등 업데이트
        boolean success = postMapper.updateById(post) > 0;
        if (!success) return false;

        // 2. 기존 태그 연결 삭제
        tagMapper.deletePostTagsByPostId(post.getPostId());

        // 3. 새 태그 문자열 파싱 및 등록
        String tagStr = post.getTags();  // 예: "고양이, 햇살, 귀여움"
        if (tagStr != null && !tagStr.trim().isEmpty()) {
            String[] tagNames = tagStr.split(",");
            for (String rawName : tagNames) {
                String name = rawName.trim();
                if (name.isEmpty()) continue;

                // DB에 존재하는 태그인지 확인
                Integer tagId = tagMapper.findTagIdByName(name);
                if (tagId == null) {
                    // 없으면 새로 등록
                    Tag tag = new Tag();
                    tag.setName(name);
                    tagMapper.insertTag(tag);
                    tagId = tag.getTagId(); // 생성된 태그 ID
                }

                // post_tags 관계 테이블에 연결
                tagMapper.insertPostTag(post.getPostId(), tagId);
            }
        }

        return true;
    }

    @Override
    public boolean deleteById(String id) throws Exception {
        return postMapper.deleteById(id) > 0;
    }

    @Override
    public boolean isOwner(String id, Long userId) throws Exception {
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


    @Override
    public void increaseCommentCount(int postId) {
        postMapper.updateCommentCount(postId); 
    }

    @Override
    public void decreaseCommentCount(int postId) {
        postMapper.minusCommentCount(postId);
    }





}
