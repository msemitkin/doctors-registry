package org.geekhub.doctorsregistry.api.dto.clinic;

import java.util.Objects;

public class ClinicDTO {

    private final Integer id;
    private final String name;
    private final String address;

    public ClinicDTO(Integer id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClinicDTO clinicDTO = (ClinicDTO) o;
        return Objects.equals(id, clinicDTO.id) &&
               Objects.equals(name, clinicDTO.name) &&
               Objects.equals(address, clinicDTO.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address);
    }

    @Override
    public String toString() {
        return "ClinicDTO{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", address='" + address + '\'' +
               '}';
    }
}
