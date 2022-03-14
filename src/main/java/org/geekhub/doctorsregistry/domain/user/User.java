package org.geekhub.doctorsregistry.domain.user;

import org.geekhub.doctorsregistry.web.security.role.Role;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class User {
    @NonNull
    private final String email;
    @NonNull
    private final String password;
    @NonNull
    private final Role role;

    public static User newClinic(String email, String password) {
        return new User(email, password, Role.CLINIC);
    }

    public static User newDoctor(String email, String password) {
        return new User(email, password, Role.DOCTOR);
    }

    public static User newPatient(String email, String password) {
        return new User(email, password, Role.PATIENT);
    }

    private User(
        @NonNull String email,
        @NonNull String password,
        @NonNull Role role
    ) {
        this.email = Objects.requireNonNull(email);
        this.password = Objects.requireNonNull(password);
        this.role = Objects.requireNonNull(role);
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    @NonNull
    public Role getRole() {
        return role;
    }
}
