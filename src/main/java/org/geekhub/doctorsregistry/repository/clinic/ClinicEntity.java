package org.geekhub.doctorsregistry.repository.clinic;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "clinic")
public class ClinicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String address;
    private String email;

    protected ClinicEntity() {
    }

    public ClinicEntity(
        Integer id,
        String name,
        String address,
        String email
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public static ClinicEntity of(String name, String address, String email) {
        return new ClinicEntity(null, name, address, email);
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

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClinicEntity that = (ClinicEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(address, that.address) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, email);
    }

    @Override
    public String toString() {
        return "ClinicEntity{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", address='" + address + '\'' +
               ", email='" + email + '\'' +
               '}';
    }
}
