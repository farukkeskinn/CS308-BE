package edu.sabanciuniv.projectbackend.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000", "http://10.51.26.245:3000", "http://172.20.10.7:3000") // ✅ yalnızca izinli IP'ler
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true); // ✅ tarayıcıda oturum vb. için
            }
        };
    }
}