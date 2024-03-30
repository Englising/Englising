package org.englising.com.englisingbe.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**")
                .exposedHeaders("Set-Cookie")
                .allowedOrigins("https://j10a106.p.ssafy.io")
                .allowedOrigins("http://localhost:5173")
                .allowedOrigins("http://localhost:5174")
                .allowedOrigins("https://localhost:5173/")
                .allowedOrigins("https://localhost:5174/")
                .allowedOrigins("https://localhost:5175/")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
