package org.geekhub.doctorsregistry.domain.user;

public class CreateUserDTO {
    private final String email;
    private final String password;
    private final String passwordConfirmation;
    private final String[] roles;

    public CreateUserDTO(
        String email,
        String password,
        String passwordConfirmation,
        String[] roles
    ) {
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
        this.roles = roles;
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

    public String[] getRoles() {
        return roles;
    }

}
