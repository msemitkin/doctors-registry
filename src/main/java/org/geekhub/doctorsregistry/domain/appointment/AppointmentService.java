package org.geekhub.doctorsregistry.domain.appointment;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.patient.OperationNotAllowedException;
import org.geekhub.doctorsregistry.domain.patient.PatientService;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientService patientService;
    private final AppointmentValidator appointmentValidator;

    public AppointmentService(
        AppointmentRepository appointmentRepository,
        PatientService patientService,
        AppointmentValidator appointmentValidator
    ) {
        this.appointmentRepository = appointmentRepository;
        this.patientService = patientService;
        this.appointmentValidator = appointmentValidator;
    }

    public void create(AppointmentEntity appointment) {
        appointmentValidator.validate(appointment);
        appointmentRepository.create(appointment);
    }

    public AppointmentEntity findById(int id) {
        return appointmentRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

    public void deleteById(int patientId, Integer appointmentId) {
        if (isAppointmentPending(patientId, appointmentId)) {
            appointmentRepository.deleteById(appointmentId);
        } else {
            throw new OperationNotAllowedException();
        }
    }

    private boolean isAppointmentPending(@NonNull Integer patientId, @NonNull Integer appointmentId) {
        List<AppointmentEntity> appointments = patientService.getPendingAppointments(patientId);
        return appointments.stream()
            .map(AppointmentEntity::getId)
            .anyMatch(appointmentId::equals);
    }

}
