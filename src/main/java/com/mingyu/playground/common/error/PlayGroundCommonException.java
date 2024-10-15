package com.mingyu.playground.common.error;

import lombok.Getter;

@Getter
public class PlayGroundCommonException extends RuntimeException {

    private final String code;

    public PlayGroundCommonException(String code, String message) {
        super(message);
        this.code = code;
    }

    public PlayGroundCommonException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
