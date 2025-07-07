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
        // 전체 게시글 수 조회 후 pagination 계산
        long total = postMapper.count(pagination);
        pagination.setTotal(total);

        List<Posts> postList = postMapper.page(pagination);

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

        return postList;
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
        boolean success = postMapper.insert(post) > 0;
        if (!success) return false;

        // 썸네일 추출
        String content = post.getContent();
        if (content != null) {
            java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("<img[^>]+src=[\"']?([^\"'>]+)[\"']?")
                .matcher(content);
            if (matcher.find()) {
                String firstImgSrc = matcher.group(1);
                post.setThumbnailUrl(firstImgSrc);
                postMapper.updateThumbnail(post);
            }
        }

        // 태그 처리
        String tagStr = post.getTags();
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
                    tagId = tag.getTagId();
                }

                tagMapper.insertPostTag(post.getPostId(), tagId);
            }
        }

        return true;
    }

    @Override
    public boolean updateById(Posts post) throws Exception {
        boolean success = postMapper.updateById(post) > 0;
        if (!success) return false;

        tagMapper.deletePostTagsByPostId(post.getPostId());

        String tagStr = post.getTags();
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
                    tagId = tag.getTagId();
                }

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

    @Override
    public List<Posts> pageBySearch(String type, String keyword, Pagination pagination) throws Exception {
        return postMapper.pageBySearch(type, keyword, pagination);
    }

    @Override
    public long countBySearch(String type, String keyword) throws Exception {
        return postMapper.countBySearch(type, keyword);
    }


    @Override
    public List<Posts> pageByPopularity(Pagination pagination) throws Exception {
        long total = postMapper.count(pagination);
        pagination.setTotal(total);
        List<Posts> postList = postMapper.pageByPopularity(pagination);

        // 태그 매핑 (기존 코드 재사용)
        if (!postList.isEmpty()) {
            List<Integer> postIds = postList.stream().map(Posts::getPostId).toList();
            List<Tag> tagResults = postMapper.selectTagsByPostIds(postIds);
            Map<Integer, List<Tag>> tagMap = tagResults.stream().collect(Collectors.groupingBy(Tag::getPostId));
            for (Posts post : postList) {
                List<Tag> tagList = tagMap.get(post.getPostId());
                if (tagList != null) post.setTagList(tagList);
            }
        }

        return postList;
    }
}
