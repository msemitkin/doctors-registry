package org.geekhub.doctorsregistry.domain.mapper;

import org.geekhub.doctorsregistry.repository.specialization.SpecializationEntity;
import org.geekhub.doctorsregistry.web.dto.specialization.SpecializationDTO;
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
