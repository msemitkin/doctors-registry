package org.geekhub.doctorsregistry.repository.doctor;

import org.geekhub.doctorsregistry.repository.specialization.SpecializationEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "doctor")
public class DoctorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "specialization_id")
    private SpecializationEntity specialization;
    private Integer clinicId;
    private Integer price;

    protected DoctorEntity() {
    }

    public DoctorEntity(
        Integer id,
        String firstName,
        String lastName,
        SpecializationEntity specialization,
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

    public static DoctorEntity withId(int id) {
        return new DoctorEntity(id, null, null, null, null, null);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public SpecializationEntity getSpecialization() {
        return specialization;
    }

    public void setSpecialization(SpecializationEntity specialization) {
        this.specialization = specialization;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorEntity that = (DoctorEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(specialization, that.specialization) && Objects.equals(clinicId, that.clinicId) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, specialization, clinicId, price);
    }

    @Override
    public String toString() {
        return "DoctorEntity{" +
               "id=" + id +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", specialization=" + specialization +
               ", clinicId=" + clinicId +
               ", price=" + price +
               '}';
    }
}
