package com.mingyu.playground.config;

import com.mingyu.playground.filter.AuthorizationHeaderFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class WebFluxSecurityConfig {

    // All Allowed Pages
    String[] allAllowPages = {
            "/swagger-ui/**",     // Swagger UI 관련 리소스
            "/v3/api-docs/**",     // Swagger API 문서 리소스
            "/swagger-resources/**" // Swagger 추가 리소스
    };

    // Un Login User Allowed Pages
    String[] unLoginUserAllowedPages = {
            "/api/v1/auth/login", // 로그인 API,
            "/api/v1/auth/signup", // 회원가입 API
    };

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {

        serverHttpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .anonymous(ServerHttpSecurity.AnonymousSpec::disable);

        serverHttpSecurity.authorizeExchange((authorize) -> authorize
                .pathMatchers(allAllowPages).permitAll()
                .pathMatchers(unLoginUserAllowedPages).permitAll()
                .anyExchange().authenticated()
        );

        return serverHttpSecurity.build();

    }
}
