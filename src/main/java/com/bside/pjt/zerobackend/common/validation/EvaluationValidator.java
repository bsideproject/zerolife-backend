package com.bside.pjt.zerobackend.common.validation;

import com.bside.pjt.zerobackend.common.ErrorCode;
import com.bside.pjt.zerobackend.mission.domain.Evaluation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EvaluationValidator implements ConstraintValidator<ValidEvaluation, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() == 0) {
            changeConstraintViolation(ErrorCode.E3008, context);
            return false;
        }

        try {
            Evaluation.valueOf(value);
        } catch (Exception e) {
            changeConstraintViolation(ErrorCode.E3009, context);
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
