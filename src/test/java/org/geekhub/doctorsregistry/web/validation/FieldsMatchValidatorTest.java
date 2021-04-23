package org.geekhub.doctorsregistry.web.validation;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class FieldsMatchValidatorTest {

    private Validator validator;

    @FieldsMatch(first = "first", second = "second")
    static class FieldsMatchTestDTO {
        private final String first;
        private final String second;

        public FieldsMatchTestDTO(String first, String second) {
            this.first = first;
            this.second = second;
        }

        public String getFirst() {
            return first;
        }

        public String getSecond() {
            return second;
        }
    }

    @BeforeMethod
    private void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void does_not_produce_errors_when_fields_are_equal() {
        FieldsMatchTestDTO fieldsMatchTestDTO = new FieldsMatchTestDTO("string", "string");
        Set<ConstraintViolation<FieldsMatchTestDTO>> violations = validator.validate(fieldsMatchTestDTO);
        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void produces_errors_when_fields_are_not_equal() {
        FieldsMatchTestDTO fieldsMatchTestDTO = new FieldsMatchTestDTO("string", "second-string");
        Set<ConstraintViolation<FieldsMatchTestDTO>> violations = validator.validate(fieldsMatchTestDTO);
        Assert.assertEquals(violations.size(), 2);
    }

}