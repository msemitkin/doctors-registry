package org.geekhub.doctorsregistry.domain.appointment;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.datime.ZonedTime;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Service
public class AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    private final ZonedTime zonedTime;
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(
        ZonedTime zonedTime,
        AppointmentRepository appointmentRepository
    ) {
        this.zonedTime = zonedTime;
        this.appointmentRepository = appointmentRepository;
    }

    public void create(AppointmentEntity appointmentEntity) {

        Assert.notNull(appointmentEntity, "Appointment entity is null");
        Assert.notNull(appointmentEntity.getDoctorId(), "Doctor id is null");
        Assert.notNull(appointmentEntity.getPatientId(), "Patient id is null");
        Assert.notNull(appointmentEntity.getDateTime(), "Datetime is null");

        if (!appointmentEntity.getDateTime().isAfter(zonedTime.now())) {
            logger.warn("Cannot create appointment for past time");
            throw new IllegalArgumentException("Cannot create appointment for past time");
        }
        if (!patientDoNotHaveAppointment(appointmentEntity)) {
            logger.warn("Patient already has an appointment at specified time");
            throw new PatientBusyException("Patient already has an appointment at specified time");
        }
        if (!doctorAvailable(appointmentEntity.getDoctorId(), appointmentEntity.getDateTime())) {
            logger.info("Attempted to make an appointment with a doctor at already booked time");
            throw new DoctorNotAvailableException("Doctor is not available at this time");
        }
        appointmentRepository.create(appointmentEntity);
    }

    public AppointmentEntity findById(Integer id) {
        return appointmentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    private boolean doctorAvailable(Integer doctorId, LocalDateTime localDateTime) {
        return appointmentRepository.doctorAvailable(doctorId, localDateTime);
    }

    private boolean patientDoNotHaveAppointment(AppointmentEntity appointmentEntity) {
        return appointmentRepository.patientDoNotHaveAppointment(
            appointmentEntity.getPatientId(), appointmentEntity.getDateTime()
        );
    }

}
