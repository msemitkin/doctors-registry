package org.geekhub.doctorsregistry.web.api.clinic;

public record ClinicDTO(
    Integer id,
    String name,
    String address
) {

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
