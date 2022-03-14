package org.geekhub.doctorsregistry.domain.mapper;

import org.geekhub.doctorsregistry.repository.clinic.ClinicEntity;
import org.geekhub.doctorsregistry.web.dto.clinic.ClinicDTO;
import org.springframework.stereotype.Component;

@Component
public class ClinicMapper {

    public ClinicDTO toDTO(ClinicEntity entity) {
        return new ClinicDTO(
            entity.getId(),
            entity.getName(),
            entity.getAddress()
        );
    }

}
