import org.apache.catalina.Context;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.annotation.MultipartConfig;

@Configuration
public class TomcatCustomizer {

    @Bean
    public TomcatServletWebServerFactory tomcatFactory() {
        return new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                MultipartConfig multipartConfig = new MultipartConfig();
                multipartConfig.setMaxFileSize("10485760"); // 10MB
                multipartConfig.setMaxRequestSize("104857600"); // 100MB
                multipartConfig.setFileCountThreshold(20); // 파일 개수 제한 설정
                context.setMultipartConfigDescriptor(multipartConfig);
            }
        };
    }
}
