package org.geekhub.doctorsregistry.repository.doctor;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends CrudRepository<DoctorEntity, Integer> {

    List<DoctorEntity> findDoctorEntitiesByClinicId(Integer clinicId);
}
