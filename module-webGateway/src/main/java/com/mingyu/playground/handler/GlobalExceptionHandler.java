package com.mingyu.playground.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mingyu.playground.errors.PlayGroundCommonException;
import com.mingyu.playground.errors.PlayGroundErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@Order(-2)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        // 사용자 정의 오류 응답
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String errorCode = PlayGroundErrorCode.JWT_COMMON_ERROR.getCode();
        String errorMessage = PlayGroundErrorCode.JWT_COMMON_ERROR.getMessage();

        if (ex instanceof ExpiredJwtException) { // JWT 만료
            httpStatus = HttpStatus.UNAUTHORIZED;
            errorCode = PlayGroundErrorCode.JWT_EXPIRED_ERROR.getCode();
            errorMessage = PlayGroundErrorCode.JWT_EXPIRED_ERROR.getMessage();
        } else if (ex instanceof Exception) { // 다른 오류들 추후 정리
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorCode = PlayGroundErrorCode.COMMON_FAIL.getCode();
            errorMessage = PlayGroundErrorCode.COMMON_FAIL.getMessage();
        }

        response.setStatusCode(httpStatus);

        PlayGroundCommonException playGroundCommonException = new PlayGroundCommonException(errorCode, errorMessage);

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(playGroundCommonException);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);

            return response.writeWith(Mono.just(buffer));
        } catch (Exception e) {

            return response.setComplete();
        }
    }
}
