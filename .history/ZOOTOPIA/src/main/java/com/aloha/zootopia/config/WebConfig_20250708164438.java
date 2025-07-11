package com.aloha.zootopia.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///C:/upload/");
    }


    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(connector -> {
            connector.setMaxPostSize(50 * 1024 * 1024); // 50MB
            connector.setProperty("maxParameterCount", "10000"); // ⚠️ 파일 수 제한 (기본 1000)
        });
        return factory;
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> containerCustomizer() {
        return factory -> factory.addConnectorCustomizers(connector -> {
            connector.setProperty("maxParameterCount", "10000"); // 기본 1000 → 10000으로 증가
        });
    }

    @Bean
public WebServerFactoryCustomizer<TomcatServletWebServerFactory> customizer() {
    return factory -> factory.addConnectorCustomizers(connector -> {
        connector.setMaxPostSize(50 * 1024 * 1024); // POST 최대 크기 (50MB)
        connector.setProperty("maxParameterCount", "10000"); // 파라미터 수 제한
    });
}


}   