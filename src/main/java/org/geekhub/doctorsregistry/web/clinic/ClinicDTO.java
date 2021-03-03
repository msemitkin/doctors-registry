package org.geekhub.doctorsregistry.web.clinic;

import org.geekhub.doctorsregistry.repository.clinic.ClinicEntity;

public record ClinicDTO(
    Integer id,
    String name,
    String address
) {
    public static ClinicDTO of(ClinicEntity entity) {
        return new ClinicDTO(entity.getId(), entity.getName(), entity.getAddress());
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
