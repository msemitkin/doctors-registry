package org.geekhub.doctorsregistry.web.dto.patient;

public interface CreatePatientDTO {

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    String getEmail();

    void setEmail(String email);
}
