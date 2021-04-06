package org.geekhub.doctorsregistry.domain.appointment;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.patient.PatientService;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentRepository;
import org.geekhub.doctorsregistry.repository.patient.PatientJdbcTemplateRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

}
