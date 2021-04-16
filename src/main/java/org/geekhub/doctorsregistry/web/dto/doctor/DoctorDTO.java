package org.geekhub.doctorsregistry.web.dto.doctor;

import org.geekhub.doctorsregistry.web.api.specialization.SpecializationDTO;

import java.util.Objects;

public class DoctorDTO {

    private final Integer id;
    private final String firstName;
    private final String lastName;
    private final SpecializationDTO specialization;
    private final Integer clinicId;
    private final Integer price;

    public DoctorDTO(
        Integer id,
        String firstName,
        String lastName,
        SpecializationDTO specialization,
        Integer clinicId,
        Integer price
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.clinicId = clinicId;
        this.price = price;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorDTO dto = (DoctorDTO) o;
        return Objects.equals(id, dto.id) &&
               Objects.equals(firstName, dto.firstName) &&
               Objects.equals(lastName, dto.lastName) &&
               Objects.equals(specialization, dto.specialization) &&
               Objects.equals(clinicId, dto.clinicId) &&
               Objects.equals(price, dto.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, specialization, clinicId, price);
    }

    @Override
    public String toString() {
        return "DoctorDTO{" +
               "id=" + id +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", specialization=" + specialization +
               ", clinicId=" + clinicId +
               ", price=" + price +
               '}';
    }
}
