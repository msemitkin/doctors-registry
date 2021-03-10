package org.geekhub.doctorsregistry.web.api.clinic;

import org.geekhub.doctorsregistry.repository.clinic.ClinicEntity;
import org.springframework.stereotype.Component;

@Component
public class ClinicMapper {

    public ClinicEntity toEntity(ClinicDTO clinicDTO) {
        return new ClinicEntity(
            clinicDTO.getId(),
            clinicDTO.getName(),
            clinicDTO.getAddress()
        );
    }

    public ClinicDTO toDTO(ClinicEntity entity) {
        return new ClinicDTO(
            entity.getId(),
            entity.getName(),
            entity.getAddress()
        );
    }

}
