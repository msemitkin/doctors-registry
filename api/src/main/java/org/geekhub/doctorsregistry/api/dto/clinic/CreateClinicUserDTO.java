package org.geekhub.doctorsregistry.api.dto.clinic;

import org.geekhub.doctorsregistry.api.dto.user.CreateUserDTO;
import org.geekhub.doctorsregistry.api.validation.FieldsMatch;
import org.geekhub.doctorsregistry.api.validation.UniqueEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@FieldsMatch(first = "password", second = "passwordConfirmation", message = "Passwords do not match")
public class CreateClinicUserDTO implements CreateClinicDTO, CreateUserDTO {

    @NotNull
    @NotBlank(message = "Name is required")
    private String name;
    @NotNull
    @NotBlank(message = "Address is required")
    private String address;
    @NotNull
    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    @UniqueEmail
    private String email;
    @NotNull
    @NotBlank(message = "Password is required")
    private String password;
    @NotNull
    @NotBlank(message = "Password confirmation is required")
    private String passwordConfirmation;

    public CreateClinicUserDTO() {
    }

    public CreateClinicUserDTO(
        String name,
        String address,
        String email,
        String password,
        String passwordConfirmation
    ) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateClinicUserDTO that = (CreateClinicUserDTO) o;
        return Objects.equals(name, that.name) &&
               Objects.equals(address, that.address) &&
               Objects.equals(email, that.email) &&
               Objects.equals(password, that.password) &&
               Objects.equals(passwordConfirmation, that.passwordConfirmation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, email, password, passwordConfirmation);
    }

    @Override
    public String toString() {
        return "CreateClinicUserDTO{" +
               "name='" + name + '\'' +
               ", address='" + address + '\'' +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' +
               ", passwordConfirmation='" + passwordConfirmation + '\'' +
               '}';
    }
}
