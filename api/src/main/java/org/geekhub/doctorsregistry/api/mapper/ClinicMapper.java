package org.geekhub.doctorsregistry.api.mapper;

import org.geekhub.doctorsregistry.api.dto.clinic.ClinicDTO;
import org.geekhub.doctorsregistry.api.dto.clinic.CreateClinicDTO;
import org.geekhub.doctorsregistry.repository.clinic.ClinicEntity;
import org.springframework.stereotype.Component;

@Component
public class ClinicMapper {

    public ClinicEntity toEntity(CreateClinicDTO clinicDTO) {
        return ClinicEntity.of(clinicDTO.getName(), clinicDTO.getAddress(), clinicDTO.getEmail());
    }

    public ClinicDTO toDTO(ClinicEntity entity) {
        return new ClinicDTO(
            entity.getId(),
            entity.getName(),
            entity.getAddress()
        );
    }

}
