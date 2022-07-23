package com.bside.pjt.zerobackend.common.validation;

import com.bside.pjt.zerobackend.common.ErrorCode;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private final String PATTERN = "(?=.*[a-zA-Z])(?=.*[0-9]).*";
    private final int MIN = 8;
    private final int MAX = 16;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            changeConstraintViolation(ErrorCode.E1008, context);
            return false;
        }

        if (!Pattern.matches(PATTERN, value)) {
            changeConstraintViolation(ErrorCode.E1009, context);
            return false;
        }

        if (value.length() < MIN) {
            changeConstraintViolation(ErrorCode.E1010, context);
            return false;
        }

        if (value.length() > MAX) {
            changeConstraintViolation(ErrorCode.E1011, context);
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
