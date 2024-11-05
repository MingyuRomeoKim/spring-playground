package com.mingyu.playground.config;

import com.mingyu.playground.util.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class WebFluxSecurityConfig {

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

        serverHttpSecurity.logout(logoutSpec -> {
            logoutSpec.logoutUrl("/api/v1/auth/logout")
                    .requiresLogout(new PathPatternParserServerWebExchangeMatcher("/api/v1/auth/logout", HttpMethod.POST));
        });
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


