package org.geekhub.doctorsregistry.api.dto.patient;

public class PatientDTO {

    private final Integer id;
    private final String firstName;
    private final String lastName;
    private final String email;

    public PatientDTO(
        Integer id,
        String firstName,
        String lastName,
        String email
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Integer getId() {
        return id;
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
