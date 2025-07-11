import jakarta.servlet.MultipartConfigElement;
import org.apache.catalina.Context;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultipartTomcatConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> factory.addContextCustomizers((Context context) -> {
            MultipartConfigElement multipartConfigElement =
                    new MultipartConfigElement(null, 20 * 1024 * 1024, 40 * 1024 * 1024, 0);
            context.setMultipartConfig(multipartConfigElement);
        });
    }
}
