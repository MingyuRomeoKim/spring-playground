package com.mingyu.playground.config;

import com.mingyu.playground.domain.jwt.filter.TokenAuthenticationFilter;
import com.mingyu.playground.domain.jwt.util.JwtTokenizer;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // All Allowed Pages
    String[] allAllowPages = {
            "/swagger-ui/**",     // Swagger UI 관련 리소스
            "/v3/api-docs/**",     // Swagger API 문서 리소스
            "/swagger-resources/**" // Swagger 추가 리소스
    };

    // Un Login User Allowed Pages
    String[] unLoginUserAllowedPages = {
            "/api/v1/auth/login" // 로그인 API,
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtTokenizer jwtTokenizer) throws Exception {
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
                // 로그인하지 않은 사용자 접근 허용
                .requestMatchers(unLoginUserAllowedPages).permitAll()
                // 이외의 모든 요청은 인증 정보 필요
                .anyRequest().authenticated()
        );

        // JWT 필터 사용
        httpSecurity.addFilter(this.corsFilter());
        httpSecurity.addFilterBefore(new TokenAuthenticationFilter(jwtTokenizer), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedHeader("Authorization");
        config.addAllowedHeader("access_token_key");
        config.addAllowedHeader("X-Requested-With");
        config.addAllowedHeader("Server Authorization");
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("Content-Length");
        config.addAllowedHeader("Cache-Control");

        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");

        config.addAllowedOriginPattern("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
