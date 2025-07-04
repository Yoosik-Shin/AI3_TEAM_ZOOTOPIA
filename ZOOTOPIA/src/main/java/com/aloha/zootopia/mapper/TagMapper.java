package com.aloha.zootopia.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aloha.zootopia.domain.Tag;

@Mapper
public interface TagMapper {
    Integer findTagIdByName(String name);
    int insertTag(Tag tag); 
    int insertPostTag(@Param("postId") int postId, @Param("tagId") int tagId);
    List<String> getTagsByPostId(int postId);

    void deletePostTagsByPostId(int postId);
}

