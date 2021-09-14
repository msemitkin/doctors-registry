package org.geekhub.doctorsregistry;

import org.geekhub.doctorsregistry.web.security.role.Role;
import org.testng.annotations.DataProvider;

import java.util.Arrays;

public class RolesDataProviders {

    @DataProvider(name = "roles_except_admin")
    public Object[][] roles_except_admin() {
        return Arrays.stream(Role.values())
            .filter(role -> !role.equals(Role.ADMIN))
            .map(role -> new Object[]{role})
            .toArray(Object[][]::new);
    }

    @DataProvider(name = "roles_except_clinic")
    public Object[][] roles_except_clinic() {
        return Arrays.stream(Role.values())
            .filter(role -> !role.equals(Role.CLINIC))
            .map(role -> new Object[]{role})
            .toArray(Object[][]::new);
    }

    @DataProvider(name = "roles_except_doctor")
    public Object[][] roles_except_doctor() {
        return Arrays.stream(Role.values())
            .filter(role -> !role.equals(Role.DOCTOR))
            .map(role -> new Object[]{role})
            .toArray(Object[][]::new);
    }

    @DataProvider(name = "roles_except_patient")
    public Object[][] roles_except_patient() {
        return Arrays.stream(Role.values())
            .filter(role -> !role.equals(Role.PATIENT))
            .map(role -> new Object[]{role})
            .toArray(Object[][]::new);
    }

    @DataProvider(name = "roles")
    public Object[][] roles() {
        return Arrays.stream(Role.values())
            .map(role -> new Object[]{role})
            .toArray(Object[][]::new);
    }

}
