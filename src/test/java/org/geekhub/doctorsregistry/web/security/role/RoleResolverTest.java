package org.geekhub.doctorsregistry.web.security.role;

import org.assertj.core.api.Assertions;
import org.geekhub.doctorsregistry.web.dto.clinic.CreateClinicUserDTO;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.geekhub.doctorsregistry.web.dto.patient.CreatePatientUserDTO;
import org.geekhub.doctorsregistry.web.dto.user.CreateUserDTO;
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
        Assertions.assertThatCode(() -> roleResolver.resolveRole(null))
            .isInstanceOf(IllegalArgumentException.class);
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

}