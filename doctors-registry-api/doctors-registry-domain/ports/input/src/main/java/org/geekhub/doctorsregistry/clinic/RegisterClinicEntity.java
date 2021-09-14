package org.geekhub.doctorsregistry.clinic;

public class RegisterClinicEntity {

    private final String name;
    private final String address;
    private final String email;

    public RegisterClinicEntity(
        String name,
        String address,
        String email
    ) {
        this.name = name;
        this.address = address;
        this.email = email;
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
}
