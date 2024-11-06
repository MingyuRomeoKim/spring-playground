package com.mingyu.playground.filter;

import com.mingyu.playground.service.AuthLoginService;
import com.mingyu.playground.util.JwtTokenizer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final JwtTokenizer jwtTokenizer;
    private final AuthLoginService authLoginService;

    @Autowired
    public AuthorizationHeaderFilter(JwtTokenizer jwtTokenizer, AuthLoginService authLoginService) {
        super(Config.class);
        this.jwtTokenizer = jwtTokenizer;
        this.authLoginService = authLoginService;
    }

    @Override
    public GatewayFilter apply(AuthorizationHeaderFilter.Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("request uri : {}", request.getURI());

            log.warn("request headers : {}", request.getHeaders());

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                response.setStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
                return response.setComplete();
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");
            log.info("jwt : {}", jwt);

            // JWT 검증
            if (!isJwtValid(jwt)) {
                response.setStatusCode(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
                return response.setComplete();
            }

            // Logout Token 검증
            if (isLogoutToken(jwt)) {
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return response.setComplete();
            }

            return chain.filter(exchange);

        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        log.error(err);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        return response.setComplete();
    }

    private boolean isJwtValid(String jwt) {

        Claims claims = jwtTokenizer.parseAccessToken(jwt); // 토큰에서 클레임을 파싱

        if (claims == null || Strings.isBlank(claims.getSubject())) { // 클레임이 없거나 이메일이 없으면 false
            return false;
        }

        String email = claims.getSubject(); // 이메일을 가져옴
        String id = claims.get("id", String.class); // 사용자 ID를 가져옴
        String name = claims.get("name", String.class); // 이름을 가져옴
        return true;
    }

    private boolean isLogoutToken(String jwt) {
        return !authLoginService.isLogin(jwt);
    }

    public static class Config {}
}
