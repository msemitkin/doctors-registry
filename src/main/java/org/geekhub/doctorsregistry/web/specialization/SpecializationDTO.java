package org.geekhub.doctorsregistry.web.specialization;

import org.geekhub.doctorsregistry.repository.specialization.SpecializationEntity;

public record SpecializationDTO(Integer id, String name) {

    public static SpecializationDTO of(SpecializationEntity entity) {
        return new SpecializationDTO(entity.getId(), entity.getName());
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
