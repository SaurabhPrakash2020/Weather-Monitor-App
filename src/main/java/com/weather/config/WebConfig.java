package com.weather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
                // Allowing both /weather and /alerts endpoints
                registry.addMapping("/weather")
                        .allowedOrigins("http://localhost:3000", "http://192.168.1.11:3000")
                        .allowedMethods("GET", "POST")
                        .allowCredentials(true);

                registry.addMapping("/alerts")
                        .allowedOrigins("http://localhost:3000", "http://192.168.1.11:3000")
                        .allowedMethods("POST")
                        .allowCredentials(true);
            }
        };
    }
}
