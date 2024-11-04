package com.mingyu.playground.errors;

public enum PlayGroundErrorCode {
    // Common
    COMMON_FAIL("E000", "실패"),
    COMMON_NOT_FOUND("E001", "데이터를 찾을 수 없습니다."),
    COMMON_ALREADY_EXISTS("E002", "이미 존재하는 데이터입니다."),

    // Jwt
    JWT_COMMON_ERROR("E100", "알수 없는 오류로 실패하였습니다."),
    JWT_INVALID_ERROR("E101", "유효하지 않은 토큰입니다."),
    JWT_EXPIRED_ERROR("E102", "토큰이 만료되었습니다."),
    JWT_AUTHENTICATION_FAILED("E103", "인증에 실패하였습니다."),
    JWT_UNSUPPORTED_ERROR("E104", "지원하지 않는 토큰입니다."),
    JWT_TOKEN_NOT_FOUND("E105", "토큰이 존재하지 않습니다."),

    // Auth
    AUTH_INVALID("E201", "유효하지 않은 사용자 정보입니다."),
    AUTH_PASSWORD_MISMATCH("E202", "비밀번호가 일치하지 않습니다."),
    AUTH_PASSWORD_CHECK_MISMATCH("E202", "비밀번호와 비밀번호확인이 일치하지 않습니다."),

    // LOG ERROR
    LOG_ERROR_WRONG_DATA("E300", "올바르지 않는 데이터입니다."),

    // JSON
    JSON_MAPPING_ERROR("E400", "JSON 매핑에 실패하였습니다."),
    JSON_PARSING_ERROR("E401", "JSON 파싱에 실패하였습니다."),
    JSON_PROCESSING_ERROR("E402", "JSON 처리에 실패하였습니다."),
    JSON_EMPTY("E403", "JSON이 비어있습니다."),

    // Email
    EMAIL_TEMPLATE_NOT_FOUND("E500", "이메일 템플릿을 찾을 수 없습니다."),
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
