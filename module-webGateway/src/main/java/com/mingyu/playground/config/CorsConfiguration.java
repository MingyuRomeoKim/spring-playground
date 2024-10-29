package com.mingyu.playground.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Slf4j
@Configuration
public class CorsConfiguration implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebFluxConfigurer.super.addCorsMappings(registry);
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedOriginPatterns("*");

    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        log.info("CorsWebFilter");
        return new CorsWebFilter(new org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource());
    }

}
