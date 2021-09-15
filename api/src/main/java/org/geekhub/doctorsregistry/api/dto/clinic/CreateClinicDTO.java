package org.geekhub.doctorsregistry.api.dto.clinic;

public interface CreateClinicDTO {
    String getName();

    void setName(String name);

    String getAddress();

    void setAddress(String address);

    String getEmail();

    void setEmail(String email);
}
