package org.geekhub.doctorsregistry.domain.user;

import org.assertj.core.api.Assertions;
import org.geekhub.doctorsregistry.web.dto.clinic.CreateClinicUserDTO;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.geekhub.doctorsregistry.web.dto.patient.CreatePatientUserDTO;
import org.geekhub.doctorsregistry.web.dto.user.CreateUserDTO;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class UserServiceTest {

    private final CreateUserDTO TEST_CLINIC_USER = new CreateClinicUserDTO("Name", "Address",
        "clinic_email@gmail.com", "password", "password");
    private final CreateUserDTO TEST_DOCTOR_USER = new CreateDoctorUserDTO("Firstname", "Lastname",
        "doctor_email@gmail.com", 1, 100, List.of("MONDAY&08:00"), "password", "password");
    private final CreateUserDTO TEST_PATIENT_USER = new CreatePatientUserDTO("Firstname", "Lastname",
        "patient_email@gmail.com", "password", "password");


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
    public void throws_UserAlreadyExistsException_when_user_with_given_email_already_exists(CreateUserDTO userDTO) {
        Mockito.when(userDetailsManager.userExists(userDTO.getEmail())).thenReturn(true);
        Assertions.assertThatCode(() -> userService.saveUser(userDTO))
            .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    public void do_not_throw_any_exception_when_given_correct_data() {
        CreateUserDTO correctUserDTO = TEST_PATIENT_USER;
        UserDetails user = User.builder()
            .username(correctUserDTO.getEmail())
            .password("encoded_password")
            .roles("PATIENT")
            .build();
        Mockito.when(userDetailsManager.userExists(correctUserDTO.getEmail())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(correctUserDTO.getPassword())).thenReturn("encoded_password");
        Mockito.doNothing().when(userDetailsManager).createUser(user);

        Assertions.assertThatCode(() -> userService.saveUser(correctUserDTO))
            .doesNotThrowAnyException();
    }
}