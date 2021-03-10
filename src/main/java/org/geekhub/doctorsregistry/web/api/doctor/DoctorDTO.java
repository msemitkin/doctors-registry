package org.geekhub.doctorsregistry.web.api.doctor;

import org.geekhub.doctorsregistry.web.api.specialization.SpecializationDTO;

public record DoctorDTO(
    Integer id,
    String firstName,
    String lastName,
    SpecializationDTO specialization,
    Integer clinicId,
    Integer price
) {

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public SpecializationDTO getSpecialization() {
        return specialization;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public Integer getPrice() {
        return price;
    }
}
