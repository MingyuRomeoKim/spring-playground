package com.mingyu.playground.validations;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class JsonMatchesValidator implements ConstraintValidator<JsonMatches, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void initialize(JsonMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        try {
            objectMapper.readTree(value); // JSON 파싱 시도
            return true;
        } catch (Exception e) {
            return false; // 파싱 실패시 JSON 아님
        }
    }
}
