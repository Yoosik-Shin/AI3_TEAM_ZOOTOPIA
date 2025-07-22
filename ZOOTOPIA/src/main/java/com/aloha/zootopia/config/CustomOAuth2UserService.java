package com.aloha.zootopia.config;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.aloha.zootopia.domain.Users;
import com.aloha.zootopia.dto.SocialDTO;
import com.aloha.zootopia.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        OAuth2User oAuth2User = super.loadUser(request);
        String provider = request.getClientRegistration().getRegistrationId();

        SocialLogin userInfo = switch (provider) {
                                    case "naver" -> new NaverUserInfo(oAuth2User.getAttributes());
                                    // case "kakao" -> new KakaoUserInfo(oAuth2User.getAttributes());
                                    default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };

        SocialDTO dto = SocialDTO.builder()
                .email(userInfo.getEmail())
                .nickname(userInfo.getName())
                .provider(userInfo.getProvider())
                .providerId(userInfo.getProviderId())
                .build();

        Users user = null;
        try {
            user = userService.findOrCreateOAuthUser(dto);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (user == null) {
        // 예외에 대한 대응: 예외 발생 시 특정 에러를 던지거나, 실패 처리를 명확히
        throw new OAuth2AuthenticationException("유저 생성/조회 실패");
    }
    return new CustomUserDetails(user, oAuth2User.getAttributes());
    }
}
