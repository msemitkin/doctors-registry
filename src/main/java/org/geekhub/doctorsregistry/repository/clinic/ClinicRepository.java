package org.geekhub.doctorsregistry.repository.clinic;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicRepository extends CrudRepository<ClinicEntity, Integer> {
}
