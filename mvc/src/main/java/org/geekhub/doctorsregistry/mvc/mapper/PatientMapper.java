package org.geekhub.doctorsregistry.mvc.mapper;

import org.geekhub.doctorsregistry.mvc.dto.patient.CreatePatientDTO;
import org.geekhub.doctorsregistry.mvc.dto.patient.PatientDTO;
import org.geekhub.doctorsregistry.repository.patient.PatientEntity;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public PatientEntity toEntity(CreatePatientDTO patientDTO) {
        return new PatientEntity(
            null,
            patientDTO.getFirstName(),
            patientDTO.getLastName(),
            patientDTO.getEmail()
        );
    }

    public PatientDTO toDTO(PatientEntity patientEntity) {
        return new PatientDTO(
            patientEntity.getId(),
            patientEntity.getFirstName(),
            patientEntity.getLastName(),
            patientEntity.getEmail()
        );
    }

}
