package com.mingyu.playground.common.error;

import com.mingyu.playground.common.response.ApiResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    /**
     * RuntimeException 공통 Advice
     * @param ex
     * @return
     */
    @ExceptionHandler(PlayGroundCommonException.class)
    public ResponseEntity<?> handleValidationExceptions(PlayGroundCommonException ex){
        ApiResponseMessage apiResponseMessage = new ApiResponseMessage();
        apiResponseMessage.setStatus("FAIL");
        apiResponseMessage.setMessage("실패");
        apiResponseMessage.setErrorCode(ex.getCode());
        apiResponseMessage.setErrorMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponseMessage);
    }
}
