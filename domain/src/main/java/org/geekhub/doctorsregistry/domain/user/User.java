package org.geekhub.doctorsregistry.domain.user;

import org.geekhub.doctorsregistry.domain.role.Role;

public class User {
    private final String email;
    private final String password;
    private final String passwordConfirmation;
    private final Role role;

    public User(
        String email,
        String password,
        String passwordConfirmation,
        Role role
    ) {
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public Role getRole() {
        return role;
    }
}
