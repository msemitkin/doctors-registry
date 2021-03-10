package org.geekhub.doctorsregistry.mapper;

import org.geekhub.doctorsregistry.repository.specialization.SpecializationEntity;
import org.geekhub.doctorsregistry.web.specialization.SpecializationDTO;
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
