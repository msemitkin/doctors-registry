package org.geekhub.doctorsregistry.repository.doctor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends CrudRepository<DoctorEntity, Integer> {

    void deleteDoctorEntitiesByClinicId(Integer clinicId);

    @Query("select d.id from DoctorEntity d where d.clinicId = :clinicId")
    List<Integer> findAllDoctorIdsByClinicId(@Param("clinicId") Integer clinicId);

    List<DoctorEntity> findDoctorEntitiesByClinicId(Integer clinicId);
}
