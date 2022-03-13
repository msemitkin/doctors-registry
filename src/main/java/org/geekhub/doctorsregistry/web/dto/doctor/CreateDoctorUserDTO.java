package org.geekhub.doctorsregistry.web.dto.doctor;

import org.geekhub.doctorsregistry.web.dto.user.CreateUserDTO;
import org.geekhub.doctorsregistry.web.security.role.Role;
import org.geekhub.doctorsregistry.web.validation.FieldsMatch;
import org.geekhub.doctorsregistry.web.validation.UniqueEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@FieldsMatch(
    first = "password",
    second = "passwordConfirmation",
    message = "Passwords do not match"
)
public class CreateDoctorUserDTO implements CreateDoctorDTO, CreateUserDTO {
    @NotNull
    @NotBlank(message = "Firstname is required")
    private String firstName;
    @NotNull
    @NotBlank(message = "Lastname is required")
    private String lastName;
    @NotNull
    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    @UniqueEmail
    private String email;
    @NotNull
    private Integer specializationId;
    @NotNull
    @Min(value = 1, message = "Price must be positive number")
    private Integer price;
    @NotNull
    @Size(min = 1, message = "Please select doctor working hours")
    private List<String> timetable;
    @NotNull
    @NotBlank(message = "Password is required")
    private String password;
    @NotNull
    @NotBlank(message = "Password confirmation is required")
    private String passwordConfirmation;

    public CreateDoctorUserDTO() {
    }

    public CreateDoctorUserDTO(
        String firstName,
        String lastName,
        String email,
        Integer specializationId,
        Integer price,
        List<String> timetable,
        String password,
        String passwordConfirmation
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.specializationId = specializationId;
        this.price = price;
        this.timetable = timetable;
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
    public Integer getSpecializationId() {
        return specializationId;
    }

    @Override
    public void setSpecializationId(Integer specializationId) {
        this.specializationId = specializationId;
    }

    @Override
    public Integer getPrice() {
        return price;
    }

    @Override
    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public List<String> getTimetable() {
        return timetable;
    }

    @Override
    public void setTimetable(List<String> timetable) {
        this.timetable = timetable;
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
    public Role getRole() {
        return Role.DOCTOR;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateDoctorUserDTO that = (CreateDoctorUserDTO) o;
        return Objects.equals(firstName, that.firstName) &&
               Objects.equals(lastName, that.lastName) &&
               Objects.equals(email, that.email) &&
               Objects.equals(specializationId, that.specializationId) &&
               Objects.equals(price, that.price) &&
               Objects.equals(timetable, that.timetable) &&
               Objects.equals(password, that.password) &&
               Objects.equals(passwordConfirmation, that.passwordConfirmation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, specializationId,
            price, timetable, password, passwordConfirmation);
    }

    @Override
    public String toString() {
        return "CreateDoctorUserDTO{" +
               "firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' +
               ", specializationId=" + specializationId +
               ", price=" + price +
               ", timetable=" + timetable +
               ", password='" + password + '\'' +
               ", passwordConfirmation='" + passwordConfirmation + '\'' +
               '}';
    }
}
