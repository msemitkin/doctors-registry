package org.geekhub.doctorsregistry.repository.clinic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicRepository extends JpaRepository<ClinicEntity, Integer> {

    @Query("SELECT c.id FROM ClinicEntity c WHERE c.email = :email")
    Integer getIdByEmail(@Param("email") String email);
}
