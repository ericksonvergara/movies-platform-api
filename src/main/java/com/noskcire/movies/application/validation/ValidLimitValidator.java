package com.noskcire.movies.application.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class ValidLimitValidator
implements ConstraintValidator<ValidLimit, Integer> {

    @Override
    public boolean isValid(
            Integer value,
            ConstraintValidatorContext context
    ) {

        if (value == null) {
            return true;
        }

        return value >= 1 && value <= 10;
    }
}
