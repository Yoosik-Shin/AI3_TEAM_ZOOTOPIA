package com.aloha.zootopia.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aloha.zootopia.domain.UserAuth;
import com.aloha.zootopia.domain.Users;

@Mapper
public interface UserMapper {

    // 회원 가입
    public int join(Users user) throws Exception;
    
    // 회원 권한 등록
    public int insertAuth(UserAuth userAuth) throws Exception;

    // 회원 조회
    public Users select(@Param("email") String email) throws Exception;

    int updatePassword(@Param("userId") Long userId, @Param("password") String password);
    
    Users findUserById(@Param("userId") Long userId);
    
    int updateUser(Users user);
}
