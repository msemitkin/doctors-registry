package org.geekhub.doctorsregistry.domain.mapper;

import org.geekhub.doctorsregistry.repository.clinic.ClinicEntity;
import org.geekhub.doctorsregistry.web.api.clinic.ClinicDTO;
import org.geekhub.doctorsregistry.web.dto.clinic.CreateClinicDTO;
import org.springframework.stereotype.Component;

@Component
public class ClinicMapper {

    public ClinicEntity toEntity(CreateClinicDTO clinicDTO) {
        return ClinicEntity.of(clinicDTO.getName(), clinicDTO.getAddress());
    }

    public ClinicDTO toDTO(ClinicEntity entity) {
        return new ClinicDTO(
            entity.getId(),
            entity.getName(),
            entity.getAddress()
        );
    }

}
