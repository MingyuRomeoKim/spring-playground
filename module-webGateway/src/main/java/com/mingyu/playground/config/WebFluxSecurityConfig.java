package com.mingyu.playground.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class WebFluxSecurityConfig {

    private final Environment environment;

    // All Allowed Pages
    String[] allAllowPages = {
            "/swagger-ui/**",     // Swagger UI 관련 리소스
            "/v3/api-docs/**",     // Swagger API 문서 리소스
            "/swagger-resources/**" // Swagger 추가 리소스
    };

    // Un Login User Allowed Pages
    String[] unLoginUserAllowedPages = {
            "/api/v1/auth/login", // 로그인 API,
            "/api/v1/auth/loout", // 로그인 API,
            "/api/v1/auth/signup", // 회원가입 API
    };

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {

        serverHttpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .anonymous(ServerHttpSecurity.AnonymousSpec::disable);

        serverHttpSecurity.authorizeExchange((authorize) -> authorize
                // 전체 접근 허용
                .pathMatchers(allAllowPages).permitAll()
                // 로그인하지 않은 사용자 접근 허용
                .pathMatchers(unLoginUserAllowedPages).permitAll()
                // 이외의 모든 요청은 인증 정보 필요
                .anyExchange().authenticated()
        );

        return serverHttpSecurity.build();

    }

}
