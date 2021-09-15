package org.geekhub.doctorsregistry.mvc.mapper;

import org.geekhub.doctorsregistry.mvc.dto.clinic.ClinicDTO;
import org.geekhub.doctorsregistry.mvc.dto.clinic.CreateClinicDTO;
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
