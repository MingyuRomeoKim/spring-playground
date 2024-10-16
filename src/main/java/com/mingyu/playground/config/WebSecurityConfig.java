package com.mingyu.playground.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // All Allowed Pages
    String[] allAllowPages = {
            "/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults()) // cors 설정
                .csrf(AbstractHttpConfigurer::disable); // csrf 비활성화

        // Session 미사용
        httpSecurity.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 로그인 폼 비활성화
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        // http 기본 인증(헤더) 비활성화
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);

        // 요청 URI별 권한 설정
        httpSecurity.authorizeHttpRequests((authorize) -> authorize
                // 전체 접근 허용
                .requestMatchers(allAllowPages).permitAll()
                // 이외의 모든 요청은 인증 정보 필요
                .anyRequest().authenticated()
        );


        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
