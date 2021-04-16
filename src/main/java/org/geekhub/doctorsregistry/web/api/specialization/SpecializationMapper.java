package org.geekhub.doctorsregistry.web.api.specialization;

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
