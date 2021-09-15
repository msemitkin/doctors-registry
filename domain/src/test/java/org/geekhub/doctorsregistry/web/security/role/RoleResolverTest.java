package org.geekhub.doctorsregistry.web.security.role;

import org.assertj.core.api.Assertions;
import org.geekhub.doctorsregistry.web.dto.clinic.CreateClinicUserDTO;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.geekhub.doctorsregistry.web.dto.patient.CreatePatientUserDTO;
import org.geekhub.doctorsregistry.web.dto.user.CreateUserDTO;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RoleResolverTest {

    private RoleResolver roleResolver;

    @BeforeMethod
    private void setUp() {
        roleResolver = new RoleResolver();
    }

    @Test
    public void throws_exception_when_given_null_dto() {
        CreateUserDTO userDTO = null;
        Assertions.assertThatCode(() -> roleResolver.resolveRole(userDTO))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void throws_exception_when_given_null_UserDetails() {
        UserDetails userDetails = null;
        Assertions.assertThatCode(() -> roleResolver.resolveRole(userDetails))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DataProvider(name = "resolves_role_correct_parameters")
    private Object[][] resolves_role_correct_parameters() {
        return new Object[][]{
            {"CLINIC", Role.CLINIC},
            {"DOCTOR", Role.DOCTOR},
            {"PATIENT", Role.PATIENT}
        };
    }

    @Test(dataProvider = "resolves_role_correct_parameters")
    public void resolves_role_correct(String userRole, Role expected) {
        UserDetails user =
            User.builder().username("username").password("password").roles(userRole).build();
        Assert.assertEquals(roleResolver.resolveRole(user), expected);
    }

    @DataProvider(name = "resolves_role_correct_CreateUserDTO_parameters")
    private Object[][] resolves_role_correct_CreateUserDTO_parameters() {
        return new Object[][]{
            {new CreateClinicUserDTO(), Role.CLINIC},
            {new CreateDoctorUserDTO(), Role.DOCTOR},
            {new CreatePatientUserDTO(), Role.PATIENT}
        };
    }

    @Test(dataProvider = "resolves_role_correct_CreateUserDTO_parameters")
    public void resolves_role_correct(CreateUserDTO userDTO, Role expected) {
        Assert.assertEquals(roleResolver.resolveRole(userDTO), expected);
    }

    @Test
    public void throws_RoleNOtSupportedException_when_given_illegal_role() {
        UserDetails user = User.builder().username("username").password("password").roles("TEAPOT").build();
        Assertions.assertThatCode(() -> roleResolver.resolveRole(user))
            .isInstanceOf(RoleNotSupportedException.class);
    }

}