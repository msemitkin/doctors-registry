package org.geekhub.doctorsregistry.repository.specialization;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecializationRepository extends CrudRepository<SpecializationEntity, Integer> {
}
