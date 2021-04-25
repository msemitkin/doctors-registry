package org.geekhub.doctorsregistry.web.dto.doctor;

import org.geekhub.doctorsregistry.web.dto.user.CreateUserDTO;

import java.util.List;
import java.util.Objects;

public class CreateDoctorUserDTO implements CreateDoctorDTO, CreateUserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private Integer specializationId;
    private Integer price;
    private List<String> timetable;
    private String password;
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
