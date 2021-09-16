package org.geekhub.doctorsregistry.domain.patient;

public class CreatePatientCommand {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String passwordConfirmation;

    public CreatePatientCommand(
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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
}
