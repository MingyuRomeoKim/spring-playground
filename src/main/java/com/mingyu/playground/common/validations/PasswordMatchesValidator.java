package com.mingyu.playground.common.validations;

import com.mingyu.playground.common.error.PlayGroundErrorCode;
import com.mingyu.playground.domain.auth.dto.request.AuthSignUpRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {


    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        AuthSignUpRequestDto authSignUpRequestDto = (AuthSignUpRequestDto) object;
        boolean isValid = authSignUpRequestDto.getPassword().equals(authSignUpRequestDto.getPasswordCheck());

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(PlayGroundErrorCode.AUTH_PASSWORD_CHECK_MISMATCH.getMessage())
                    .addConstraintViolation();
        }

        return isValid;
    }

}
