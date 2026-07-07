package com.noskcire.movies.application.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidLimitValidator.class)
public @interface ValidLimit {

    String message() default
            "El límite debe estar entre 1 y 10";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
