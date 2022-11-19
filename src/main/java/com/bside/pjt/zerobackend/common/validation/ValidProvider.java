package com.bside.pjt.zerobackend.common.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = { ProviderValidator.class })
public @interface ValidProvider {

    String message() default "E0000";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
