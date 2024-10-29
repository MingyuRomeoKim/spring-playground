package com.mingyu.playground.config;

import com.mingyu.playground.util.JwtTokenizer;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;

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
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity, JwtTokenizer jwtTokenizer) {

        serverHttpSecurity
                .cors(Customizer.withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .anonymous(ServerHttpSecurity.AnonymousSpec::disable);

        serverHttpSecurity.authorizeExchange((authorize) -> authorize
                // Gateway 에서는 AuthorizationHeaderFilter 에서 jwt를 체크하므로 authenticated 설정 불 필요. 때문에 전체 PermitAll로 설정
                .anyExchange().permitAll()
        )
                .securityContextRepository(new StatelessWebSessionSecurityContextRepository())
        ;

        return serverHttpSecurity.build();
    }

    private static class StatelessWebSessionSecurityContextRepository implements ServerSecurityContextRepository {

        private static final Mono<SecurityContext> EMPTY_CONTEXT = Mono.empty();

        @Override
        public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
            return Mono.empty();
        }

        @Override
        public Mono<SecurityContext> load(ServerWebExchange exchange) {
            return EMPTY_CONTEXT;
        }
    }

}


