package org.geekhub.doctorsregistry.domain.mapper;

import org.geekhub.doctorsregistry.repository.patient.PatientEntity;
import org.geekhub.doctorsregistry.web.dto.patient.PatientDTO;
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
