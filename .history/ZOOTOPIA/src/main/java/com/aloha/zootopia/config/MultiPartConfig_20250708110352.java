package com.aloha.zootopia.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import jakarta.servlet.MultipartConfigElement;

@Configuration
public class MultiPartConfig {
    

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(10L));
        factory.setMaxRequestSize(DataSize.ofMegabytes(100L));
        factory.setFileSizeThreshold(DataSize.ofMegabytes(1L));
        return factory.createMultipartConfig();
    }


    // Tomcat의 파일 개수 제한 해제
    @Bean
    public org.apache.catalina.ContextCustomizer customizer() {
        return context -> {
            context.setAllowCasualMultipartParsing(true); // 일부 Tomcat 설정 호환성
            context.addParameter("org.apache.tomcat.upload.fileCountMax", "100"); // 기본값은 1000
        };
    }

}
