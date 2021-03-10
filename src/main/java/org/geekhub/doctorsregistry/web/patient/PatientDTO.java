package org.geekhub.doctorsregistry.web.patient;

public record PatientDTO(
    Integer id,
    String firstName,
    String lastName
) {

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
