package org.geekhub.doctorsregistry.domain.user;

import org.assertj.core.api.Assertions;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class UserServiceTest {

    private final User TEST_CLINIC_USER = User.newClinic(
        "clinic_email@gmail.com", "password");
    private final User TEST_DOCTOR_USER = User.newDoctor(
        "doctor_email@gmail.com", "password");
    private final User TEST_PATIENT_USER = User.newPatient(
        "patient_email@gmail.com", "password");


    @Mock
    private UserDetailsManager userDetailsManager;
    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userDetailsManager, passwordEncoder);
    }

    @DataProvider(name = "throws_UserAlreadyExistsException_when_user_with_given_email_already_exists_parameters")
    private Object[][] throws_UserAlreadyExistsException_when_user_with_given_email_already_exists_parameters() {
        return new Object[][]{
            {TEST_CLINIC_USER}, {TEST_DOCTOR_USER}, {TEST_PATIENT_USER}
        };
    }

    @Test(dataProvider = "throws_UserAlreadyExistsException_when_user_with_given_email_already_exists_parameters")
    public void throws_UserAlreadyExistsException_when_user_with_given_email_already_exists(User user) {
        Mockito.when(userDetailsManager.userExists(user.getEmail())).thenReturn(true);
        Assertions.assertThatCode(() -> userService.saveUser(user))
            .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    public void do_not_throw_any_exception_when_given_correct_data() {
        User correctUser = TEST_PATIENT_USER;
        UserDetails user = org.springframework.security.core.userdetails.User.builder()
            .username(correctUser.getEmail())
            .password("encoded_password")
            .roles("PATIENT")
            .build();
        Mockito.when(userDetailsManager.userExists(correctUser.getEmail())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(correctUser.getPassword())).thenReturn("encoded_password");
        Mockito.doNothing().when(userDetailsManager).createUser(user);

        Assertions.assertThatCode(() -> userService.saveUser(correctUser))
            .doesNotThrowAnyException();
    }
}