package com.aloha.zootopia.domain;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    private long userId;        
    private String email;          
    private String password;      
    private String nickname;       
    private String intro;          
    private String phone;          
    private String profileImg;    
    private Date createdAt; 
    private int enabled;       
    
    private List<UserAuth> authList;
}
