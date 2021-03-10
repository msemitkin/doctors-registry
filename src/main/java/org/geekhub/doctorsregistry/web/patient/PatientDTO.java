package org.geekhub.doctorsregistry.web.patient;

import org.geekhub.doctorsregistry.repository.patient.PatientEntity;

public record PatientDTO(
    Integer id,
    String firstName,
    String lastName
) {
    public static PatientDTO of(PatientEntity patientEntity) {
        return new PatientDTO(
            patientEntity.getId(),
            patientEntity.getFirstName(),
            patientEntity.getLastName()
        );
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

}
