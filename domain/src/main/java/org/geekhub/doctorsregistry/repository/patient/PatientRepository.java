package org.geekhub.doctorsregistry.repository.patient;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends CrudRepository<PatientEntity, Integer> {
    @Query("SELECT p.id FROM PatientEntity p WHERE p.email = :email")
    Integer getIdByEmail(@Param("email") String email);
}
