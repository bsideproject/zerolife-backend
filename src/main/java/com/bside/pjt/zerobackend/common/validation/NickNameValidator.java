package com.bside.pjt.zerobackend.common.validation;

import com.bside.pjt.zerobackend.common.ErrorCode;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NickNameValidator implements ConstraintValidator<NickName, String> {

    private final int MIN = 2;
    private final int MAX = 20;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            changeConstraintViolation(ErrorCode.E1004, context);
            return false;
        }

        if (value.length() < MIN) {
            changeConstraintViolation(ErrorCode.E1006, context);
            return false;
        }

        if (value.length() > MAX) {
            changeConstraintViolation(ErrorCode.E1007, context);
            return false;
        }

        return true;
    }

    private void changeConstraintViolation(ErrorCode errorCode, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorCode.name())
            .addConstraintViolation();
    }
}
