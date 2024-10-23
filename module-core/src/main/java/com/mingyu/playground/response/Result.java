package com.mingyu.playground.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

@Slf4j
public final class Result {

    private Result() {
    }

    public static ResponseEntity<ApiResult> created() {
        return ResponseEntity.status(201).build();
    }

    public static ResponseEntity<ApiResult> ok() {
        return ResponseEntity.ok().build();
    }

    public static ResponseEntity<ApiResult> ok(String message) {
        Assert.hasText(message, "Parameter `message` must not be blank");
        return ok(ApiResult.message(message));
    }

    public static ResponseEntity<ApiResult> ok(ApiResult payload) {
        Assert.notNull(payload, "Parameter `payload` must not be null");
        return ResponseEntity.ok(payload);
    }

    public static <T> ResponseEntity<ApiResponseMessage<T>> ok(ApiResponseMessage<T> apiResponseMessage) {
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
    }

    public static ResponseEntity<ApiResult> forbidden() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}