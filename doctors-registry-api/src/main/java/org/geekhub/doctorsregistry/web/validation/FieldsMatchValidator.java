package org.geekhub.doctorsregistry.web.validation;


import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class FieldsMatchValidator implements ConstraintValidator<FieldsMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;
    private String message;

    @Override
    public void initialize(FieldsMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean valid;
        Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(firstFieldName);
        Object secondValue = new BeanWrapperImpl(value).getPropertyValue(secondFieldName);
        if (fieldValue != null) {
            valid = fieldValue.equals(secondValue);
        } else {
            valid = secondValue == null;
        }
        if (!valid) {
            List.of(firstFieldName, secondFieldName).forEach(
                field -> context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(field)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation()
            );
        }
        return valid;
    }
}
