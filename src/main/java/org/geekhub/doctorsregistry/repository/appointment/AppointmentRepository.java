package org.geekhub.doctorsregistry.repository.appointment;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends CrudRepository<AppointmentEntity, Integer> {
    @Query("select ae from AppointmentEntity ae where ae.patient.id = :id")
    List<AppointmentEntity> findAppointmentEntitiesByPatientId(@Param("id") Integer id);

    @Query("select ae from AppointmentEntity ae where ae.doctorWorkingHour.doctorEntity.id = :id")
    List<AppointmentEntity> findAppointmentEntitiesByDoctorId(@Param("id") Integer id);
}
