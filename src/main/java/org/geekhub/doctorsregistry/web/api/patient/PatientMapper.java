package org.geekhub.doctorsregistry.web.api.patient;

import org.geekhub.doctorsregistry.repository.patient.PatientEntity;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public PatientDTO toDTO(PatientEntity patientEntity) {
        return new PatientDTO(
            patientEntity.getId(),
            patientEntity.getFirstName(),
            patientEntity.getLastName(),
            patientEntity.getEmail()
        );
    }

}
