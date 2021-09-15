package org.geekhub.doctorsregistry.domain.appointment;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.domain.patient.OperationNotAllowedException;
import org.geekhub.doctorsregistry.domain.patient.PatientService;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentRepository;
import org.geekhub.doctorsregistry.web.dto.appointment.CreateAppointmentDTO;
import org.geekhub.doctorsregistry.web.security.UsernameExtractor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientService patientService;
    private final AppointmentValidator appointmentValidator;
    private final UsernameExtractor usernameExtractor;
    private final AppointmentMapper appointmentMapper;

    public AppointmentService(
        AppointmentRepository appointmentRepository,
        PatientService patientService,
        AppointmentValidator appointmentValidator,
        UsernameExtractor usernameExtractor,
        AppointmentMapper appointmentMapper
    ) {
        this.appointmentRepository = appointmentRepository;
        this.patientService = patientService;
        this.appointmentValidator = appointmentValidator;
        this.usernameExtractor = usernameExtractor;
        this.appointmentMapper = appointmentMapper;
    }

    public void create(CreateAppointmentDTO createAppointmentDTO) {
        String email = usernameExtractor.getPatientUsername();
        Integer patientId = patientService.getIdByEmail(email);
        AppointmentEntity appointment = appointmentMapper.toEntity(createAppointmentDTO);
        appointment.setPatientId(patientId);
        appointmentValidator.validate(appointment);
        appointmentRepository.create(appointment);
    }

    public AppointmentEntity findById(int id) {
        return appointmentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public void deleteById(Integer appointmentId) {
        String email = usernameExtractor.getPatientUsername();
        int patientId = patientService.getIdByEmail(email);
        List<AppointmentEntity> appointments = patientService.getPendingAppointments(patientId);
        if (
            appointments.stream()
                .map(AppointmentEntity::getId)
                .anyMatch(appointmentId::equals)
        ) {
            appointmentRepository.deleteById(appointmentId);
        } else {
            throw new OperationNotAllowedException();
        }
    }

}
