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
    

    // userId로 회원 조회 (추가)
    public Users selectById(@Param("userId") Long userId) throws Exception;


    int updateUser(Users user);
    
    int deleteUserAuth(@Param("email") String email);

    int deleteById(Long userId);

}
