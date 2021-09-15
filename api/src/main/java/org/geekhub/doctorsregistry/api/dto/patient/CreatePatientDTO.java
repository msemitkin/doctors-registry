package org.geekhub.doctorsregistry.api.dto.patient;

public interface CreatePatientDTO {

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    String getEmail();

    void setEmail(String email);
}
