package org.geekhub.doctorsregistry.repository.clinic;

import org.geekhub.doctorsregistry.web.clinic.ClinicDTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clinic")
public class ClinicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String address;

    public static ClinicEntity of(ClinicDTO clinicDTO) {
        return new ClinicEntity(clinicDTO.id(), clinicDTO.name(), clinicDTO.address());
    }

    protected ClinicEntity() {
    }

    public ClinicEntity(Integer id, String name, String address) {
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
    public String toString() {
        return "ClinicEntity{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", address='" + address + '\'' +
               '}';
    }
}
