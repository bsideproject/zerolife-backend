package com.bside.pjt.zerobackend.common.validation;

import com.bside.pjt.zerobackend.common.ErrorCode;
import com.bside.pjt.zerobackend.user.domain.Provider;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProviderValidator implements ConstraintValidator<ValidProvider, Provider> {

    @Override
    public boolean isValid(Provider value, ConstraintValidatorContext context) {
        if (value == null) {
            changeConstraintViolation(ErrorCode.E1017, context);
            return false;
        }

        try {
            Provider.valueOf(value.name());
        } catch (Exception e) {
            changeConstraintViolation(ErrorCode.E1018, context);
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
