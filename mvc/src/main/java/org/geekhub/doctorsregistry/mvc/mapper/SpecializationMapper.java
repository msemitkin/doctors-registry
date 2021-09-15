package org.geekhub.doctorsregistry.mvc.mapper;

import org.geekhub.doctorsregistry.mvc.dto.specialization.SpecializationDTO;
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
