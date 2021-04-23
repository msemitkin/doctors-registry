package org.geekhub.doctorsregistry.web.dto.patient;

import org.geekhub.doctorsregistry.web.dto.user.CreateUserDTO;
import org.geekhub.doctorsregistry.web.validation.FieldsMatch;
import org.geekhub.doctorsregistry.web.validation.UniqueEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@FieldsMatch(first = "password", second = "passwordConfirmation", message = "Passwords do not match")
public class CreatePatientUserDTO implements CreateUserDTO, CreatePatientDTO {

    @NotNull
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotNull
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotNull
    @NotBlank(message = "Email is required")
    @Email(message = "Email format is not valid")
    @UniqueEmail
    private String email;
    @NotNull
    @NotBlank(message = "Password is required")
    private String password;
    @NotNull
    @NotBlank(message = "Password confirmation is required")
    private String passwordConfirmation;

    public CreatePatientUserDTO() {
    }

    public CreatePatientUserDTO(
        String firstName,
        String lastName,
        String email,
        String password,
        String passwordConfirmation
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        CreatePatientUserDTO that = (CreatePatientUserDTO) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(passwordConfirmation, that.passwordConfirmation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, password, passwordConfirmation);
    }

    @Override
    public String toString() {
        return "CreatePatientUserDTO{" +
               "firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' +
               ", passwordConfirmation='" + passwordConfirmation + '\'' +
               '}';
    }
}
