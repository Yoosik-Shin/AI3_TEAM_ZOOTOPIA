package com.aloha.zootopia.domain;

import lombok.Data;

@Data
public class UserAuth {
    private int userId;
    private String email;
    private String auth;
}
