package org.geekhub.doctorsregistry.api.mapper;

import org.geekhub.doctorsregistry.api.dto.specialization.SpecializationDTO;
import org.geekhub.doctorsregistry.repository.specialization.SpecializationEntity;
import org.springframework.stereotype.Component;

@Component
public class SpecializationMapper {

    public SpecializationDTO toDTO(SpecializationEntity entity) {
        return new SpecializationDTO(
            entity.getId(),
            entity.getName()
        );
    }

}
