package com.aloha.zootopia.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import jakarta.servlet.MultipartConfigElement;

@Configuration          // 빈 등록 설정 클래스 지정
public class WebConfig implements WebMvcConfigurer {

    @Bean                   // 빈 등록
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // return NoOpPasswordEncoder.getInstance();
        // BCryptPasswordEncoder        : BCrypt 해시 알고리즘을 사용하여 비밀번호 암호화
        // NoOpPasswordEncoder          : 암호화 없이 비밀번호를 저장
        // ...
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 파트 최대 개수 설정 (기본값 1024)
        // FileCountLimitExceededException 해결을 위해 넉넉하게 설정
        factory.setMaxRequestParts(2048);
        factory.setMaxRequestParts(2048);
        factory.setMaxFileSize(DataSize.ofMegabytes(10));
        factory.setMaxRequestSize(DataSize.ofMegabytes(100));
        return factory.createMultipartConfig();
    }

    @Bean
    public FilterRegistrationBean<MultipartFilter> multipartFilter() {
        FilterRegistrationBean<MultipartFilter> registration = new FilterRegistrationBean<>();
        MultipartFilter multipartFilter = new MultipartFilter();
        registration.setFilter(multipartFilter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE); // Ensure this runs before Spring Security
        return registration;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///C:/upload/");
    }

}   