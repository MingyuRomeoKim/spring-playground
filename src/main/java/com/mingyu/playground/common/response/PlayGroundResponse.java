package com.mingyu.playground.common.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Setter
@Getter
public class PlayGroundResponse<T> {

    public static <T> ResponseEntity<ApiResponseMessage<T>> build(T resultObject) {
        return Result.ok(new ApiResponseMessage<>(resultObject));
    }

    public static <T> ResponseEntity<ApiResponseMessage<List<T>>> buildList(List<T> resultList) {
        ApiResponseMessage<List<T>> responseMessage = new ApiResponseMessage<>(resultList);
        return Result.ok(responseMessage);
    }

    public static ResponseEntity<ApiResponseMessage<Object>> ok() {
        return Result.ok(new ApiResponseMessage<>("SUCCESS", "정상처리", "", ""));
    }
}