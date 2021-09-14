package org.geekhub.doctorsregistry.patient;

public class RegisterPatientEntity {

    private final String firstName;
    private final String lastName;
    private final String email;

    public RegisterPatientEntity(
        String firstName,
        String lastName,
        String email
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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
}
