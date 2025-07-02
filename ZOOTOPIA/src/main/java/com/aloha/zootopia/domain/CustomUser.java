package com.aloha.zootopia.domain;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@Slf4j
public class CustomUser implements UserDetails {

    // 사용자 DTO
    private Users user;

    public CustomUser(Users user) {
        this.user = user;
        log.info("✅ CustomUser 생성됨 - 권한 리스트: {}", user.getAuthList());
    }

    /**
     * 🔐 권한 정보 메소드
     * ✅ UserDetails 를 CustomUser 로 구현하여,
     *    Spring Security 의 User 대신 사용자 정의 인증 객체(CustomUser)로 적용
     * ⚠ CustomUser 적용 시, 권한을 사용할 때는 'ROLE_' 붙여서 사용해야한다.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // List<UserAuth> authList = user.getAuthList();
        // List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // for (UserAuth userAuth : authList) {
        //     String auth = userAuth.getAuth();
        //     authorities.add( new SimpleGrantedAuthority(auth) );
        // }
        // return authorities;

        return user.getAuthList().stream()
                                 .map( (auth) -> new SimpleGrantedAuthority(auth.getAuth() ))
                                 .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled() == 1 ? true : false;
    }

    
    
}
