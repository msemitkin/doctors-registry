package org.geekhub.doctorsregistry.web.dto.clinic;

import org.geekhub.doctorsregistry.web.dto.user.CreateUserDTO;

public class CreateClinicUserDTO implements CreateClinicDTO, CreateUserDTO {

    private String name;
    private String address;
    private String email;
    private String password;
    private String passwordConfirmation;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    @Override
    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
