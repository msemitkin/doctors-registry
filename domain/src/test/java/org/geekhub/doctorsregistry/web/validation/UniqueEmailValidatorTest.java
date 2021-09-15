package org.geekhub.doctorsregistry.web.validation;

import org.geekhub.doctorsregistry.domain.user.UserService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidatorTest {

    private UniqueEmailValidator uniqueEmailValidator;

    @Mock
    private UserService userService;
    @Mock
    private ConstraintValidatorContext context;

    @BeforeMethod
    private void setUp() {
        MockitoAnnotations.openMocks(this);
        uniqueEmailValidator = new UniqueEmailValidator(userService);
    }

    @Test
    public void email_is_valid_when_there_is_no_user_with_this_email() {
        String email = "email@gmail.com";
        Mockito.when(userService.userExists(email)).thenReturn(false);
        Assert.assertTrue(uniqueEmailValidator.isValid(email, context));
    }

    @Test
    public void email_is_not_valid_when_there_is_a_user_with_this_email() {
        String email = "email@gmail.com";
        Mockito.when(userService.userExists(email)).thenReturn(true);
        Assert.assertFalse(uniqueEmailValidator.isValid(email, context));
    }
}