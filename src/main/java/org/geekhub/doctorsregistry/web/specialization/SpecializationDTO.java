package org.geekhub.doctorsregistry.web.specialization;

public record SpecializationDTO(Integer id, String name) {

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
