package org.geekhub.doctorsregistry.web.dto.user;

public interface CreateUserDTO {
    String getEmail();

    void setEmail(String email);

    String getPassword();

    void setPassword(String password);

    String getPasswordConfirmation();

    void setPasswordConfirmation(String passwordConfirmation);

}
