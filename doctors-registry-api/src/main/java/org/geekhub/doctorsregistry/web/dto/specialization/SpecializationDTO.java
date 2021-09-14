package org.geekhub.doctorsregistry.web.dto.specialization;

import java.util.Objects;

public class SpecializationDTO {

    private final Integer id;
    private final String name;

    public SpecializationDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecializationDTO that = (SpecializationDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "SpecializationDTO{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}
