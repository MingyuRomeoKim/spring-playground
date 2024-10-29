package com.mingyu.playground.filter;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final Environment environment;

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
            String jwt = authorizationHeader.replace("Bearer", "");
            log.info("jwt : {}", jwt);

            // JWT 검증
            if (!isJwtValid(jwt)) {
                response.setStatusCode(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
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
        String subject = null;

        try {
            subject = Jwts.parser()
                    .setSigningKey(environment.getProperty("jwt.access.secret"))
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return !Strings.isBlank(subject);
    }

    public static class Config {}
}
