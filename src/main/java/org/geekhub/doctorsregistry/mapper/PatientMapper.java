package org.geekhub.doctorsregistry.mapper;

import org.geekhub.doctorsregistry.repository.patient.PatientEntity;
import org.geekhub.doctorsregistry.web.patient.PatientDTO;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public PatientEntity toEntity(PatientDTO dto) {
        return new PatientEntity(
            dto.getId(),
            dto.getFirstName(),
            dto.getLastName()
        );
    }

    public PatientDTO toDTO(PatientEntity patientEntity) {
        return new PatientDTO(
            patientEntity.getId(),
            patientEntity.getFirstName(),
            patientEntity.getLastName()
        );
    }

}
