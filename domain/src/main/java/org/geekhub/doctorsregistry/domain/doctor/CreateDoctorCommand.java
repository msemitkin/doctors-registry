package org.geekhub.doctorsregistry.domain.doctor;

import java.util.ArrayList;
import java.util.List;

public class CreateDoctorCommand {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final Integer specializationId;
    private final Integer price;
    private final List<String> timetable;
    private final String password;
    private final String passwordConfirmation;

    public CreateDoctorCommand(
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
        this.timetable = new ArrayList<>(timetable);
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Integer getSpecializationId() {
        return specializationId;
    }

    public Integer getPrice() {
        return price;
    }

    public List<String> getTimetable() {
        return timetable;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }
}
