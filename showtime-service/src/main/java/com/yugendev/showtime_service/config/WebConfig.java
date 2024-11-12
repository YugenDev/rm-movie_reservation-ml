package com.yugendev.showtime_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://*.*.*", "https://*.anotherdomain.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "TRACE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
