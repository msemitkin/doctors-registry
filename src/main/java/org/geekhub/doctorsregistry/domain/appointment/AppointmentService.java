package org.geekhub.doctorsregistry.domain.appointment;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.datime.ZonedTime;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private static final Integer APPOINTMENT_DURATION = 20;

    private final ZonedTime zonedTime;
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(
        ZonedTime zonedTime,
        AppointmentRepository appointmentRepository
    ) {
        this.zonedTime = zonedTime;
        this.appointmentRepository = appointmentRepository;
    }

    public AppointmentEntity findById(Integer id) {
        return appointmentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public AppointmentEntity save(AppointmentEntity appointmentEntity) {
        return appointmentRepository.save(appointmentEntity);
    }

    public List<AppointmentEntity> findArchivedAppointmentsByDoctorId(Integer doctorId) {
        LocalDateTime searchUntilTime = zonedTime.now().minusMinutes(APPOINTMENT_DURATION);
        return appointmentRepository.findAppointmentEntitiesByDoctorIdAndDateTimeBefore(
            doctorId, searchUntilTime
        );
    }

    public List<AppointmentEntity> findPendingAppointmentsByDoctorId(Integer doctorId) {
        LocalDateTime searchUntilTime = zonedTime.now().minusMinutes(APPOINTMENT_DURATION);
        return appointmentRepository.findAppointmentEntitiesByDoctorIdAndDateTimeAfter(
            doctorId, searchUntilTime
        );
    }

    public List<AppointmentEntity> findArchivedAppointmentsByPatientId(Integer patientId) {
        LocalDateTime searchUntilTime = zonedTime.now().minusMinutes(APPOINTMENT_DURATION);
        return appointmentRepository.findAppointmentEntitiesByPatientIdAndDateTimeBefore(
            patientId, searchUntilTime
        );
    }

    public List<AppointmentEntity> findPendingAppointmentsByPatientId(Integer patientId) {
        LocalDateTime searchUntilTime = zonedTime.now().minusMinutes(APPOINTMENT_DURATION);
        return appointmentRepository.findAppointmentEntitiesByPatientIdAndDateTimeAfter(
            patientId, searchUntilTime
        );
    }

}
