package com.mate.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.BeanUtils;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {

        try {
            String secondValue = BeanUtils.getProperty(value, secondFieldName);
            String firstValue = BeanUtils.getProperty(value, firstFieldName);

            return firstValue != null && firstValue.equals(secondValue);

        } catch (Exception e) {
            return false;
        }

    }
}
