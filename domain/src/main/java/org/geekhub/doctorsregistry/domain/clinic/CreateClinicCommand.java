package org.geekhub.doctorsregistry.domain.clinic;

public class CreateClinicCommand {

    private final String name;
    private final String address;
    private final String email;
    private final String password;
    private final String passwordConfirmation;

    public CreateClinicCommand(
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

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
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
