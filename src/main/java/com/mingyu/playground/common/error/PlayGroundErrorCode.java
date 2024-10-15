package com.mingyu.playground.common.error;

public enum PlayGroundErrorCode {

    COMMON_FAIL("E000", "실패"),
    COMMON_NOT_FOUND("E001", "데이터를 찾을 수 없습니다."),

    ;

    private final String code;
    private final String message;

    PlayGroundErrorCode(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
