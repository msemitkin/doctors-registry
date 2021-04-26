package org.geekhub.doctorsregistry.repository.doctor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends CrudRepository<DoctorEntity, Integer> {
    List<DoctorEntity> findDoctorEntitiesByClinicId(Integer clinicId);

    @Query("SELECT d.id FROM DoctorEntity d WHERE d.email = :email")
    Integer getIdByEmail(@Param("email") String email);
}
