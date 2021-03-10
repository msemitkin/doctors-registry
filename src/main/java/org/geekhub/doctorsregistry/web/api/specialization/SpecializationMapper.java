package org.geekhub.doctorsregistry.web.api.specialization;

import org.geekhub.doctorsregistry.repository.specialization.SpecializationEntity;
import org.springframework.stereotype.Component;

@Component
public class SpecializationMapper {

    public SpecializationEntity toEntity(SpecializationDTO specializationDTO) {
        return new SpecializationEntity(
            specializationDTO.getId(),
            specializationDTO.getName()
        );
    }

    public SpecializationDTO toDTO(SpecializationEntity entity) {
        return new SpecializationDTO(
            entity.getId(),
            entity.getName()
        );
    }

}
