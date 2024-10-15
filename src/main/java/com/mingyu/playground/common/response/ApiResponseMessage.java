package com.mingyu.playground.common.response;


import com.mingyu.playground.common.error.PlayGroundErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseMessage<T> {
    @Schema(description = "응답 코드", example = "SUCCESS")
    private String status;
    @Schema(description = "응답 메시지", example = "정상처리")
    private String message;
    @Schema(description = "오류 코드", example = " ")
    private String errorCode;
    @Schema(description = "오류 메시지", example = " ")
    private String errorMessage;
    @Schema(description = "응답 데이터")
    private T result;

    public ApiResponseMessage(String status, String message, String errorCode, String errorMessage){
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ApiResponseMessage(T result){
        this.status = "SUCCESS";
        this.message = "정상처리";
        this.result = result;
    }

    public void setError(PlayGroundErrorCode errorCode) {
        this.errorCode = errorCode.getCode();
        this.errorMessage = errorCode.getMessage();
    }

}

