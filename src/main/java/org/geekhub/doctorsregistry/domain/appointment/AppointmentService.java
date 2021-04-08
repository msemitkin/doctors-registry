package org.geekhub.doctorsregistry.domain.appointment;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.datime.ZonedTime;
import org.geekhub.doctorsregistry.domain.patient.OperationNotAllowedException;
import org.geekhub.doctorsregistry.domain.patient.PatientService;
import org.geekhub.doctorsregistry.repository.DatabaseException;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientService patientService;
    private final AppointmentValidator appointmentValidator;
    private final ZonedTime zonedTime;

    public AppointmentService(
        AppointmentRepository appointmentRepository,
        PatientService patientService,
        AppointmentValidator appointmentValidator,
        ZonedTime zonedTime) {
        this.appointmentRepository = appointmentRepository;
        this.patientService = patientService;
        this.appointmentValidator = appointmentValidator;
        this.zonedTime = zonedTime;
    }

    public void create(User user, Integer doctorId, LocalDateTime dateTime) {
        Integer patientId = patientService.getIdByEmail(user.getUsername());
        AppointmentEntity appointment = new AppointmentEntity(null, patientId, doctorId, dateTime);
        create(appointment);
    }

    public void create(AppointmentEntity appointmentEntity) {
        appointmentValidator.validate(appointmentEntity);
        appointmentRepository.create(appointmentEntity);
    }

    public AppointmentEntity findById(Integer id) {
        return appointmentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public void deleteById(String email, Integer appointmentId) {
        int patientId = patientService.getIdByEmail(email);
        List<AppointmentEntity> appointments = patientService.getPendingAppointments(patientId);
        if (
            appointments.stream()
                .map(AppointmentEntity::getId)
                .anyMatch(appointmentId::equals)
        ) {
            deleteById(appointmentId);
        } else {
            throw new OperationNotAllowedException();
        }
    }

    public void deleteById(Integer id) {
        AppointmentEntity appointment = appointmentRepository.findById(id).orElseThrow(DatabaseException::new);
        LocalDateTime appointmentDateTime = appointment.getDateTime();
        if (appointmentDateTime.isAfter(zonedTime.now())) {
            appointmentRepository.deleteById(id);
        }
    }

}
