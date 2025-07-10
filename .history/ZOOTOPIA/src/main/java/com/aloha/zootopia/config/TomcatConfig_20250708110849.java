package com.aloha.zootopia.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 톰캣 업로드 관련 설정
 * - 파라미터 수 및 파일 업로드 수 제한 해제/확대
 * - 대용량 multipart/form-data 전송 시 오류 방지
 */
@Configuration
public class TomcatConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                // form 필드 최대 개수 (기본 10,000)
                connector.setProperty("maxParameterCount", "20000");

                // 업로드 허용 파일 개수 (기본 1000개, Tomcat 10.1 이상 지원)
                connector.setProperty("fileCountMax", "50");

                // 요청 본문 최대 크기 (톰캣 자체 제한, Spring 설정과 별도)
                connector.setMaxPostSize(100 * 1024 * 1024); // 100MB
            }
        });
    }
}
