package org.geekhub.doctorsregistry.domain.patient;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.datime.ZonedTime;
import org.geekhub.doctorsregistry.domain.mapper.PatientMapper;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.patient.PatientEntity;
import org.geekhub.doctorsregistry.repository.patient.PatientJdbcTemplateRepository;
import org.geekhub.doctorsregistry.repository.patient.PatientRepository;
import org.geekhub.doctorsregistry.web.dto.patient.CreatePatientUserDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientJdbcTemplateRepository patientJdbcTemplateRepository;
    private final ZonedTime zonedTime;
    private final UserService userService;
    private final PatientMapper patientMapper;

    public PatientService(
        PatientRepository patientRepository,
        PatientJdbcTemplateRepository patientJdbcTemplateRepository,
        ZonedTime zonedTime,
        UserService userService,
        PatientMapper patientMapper
    ) {
        this.patientRepository = patientRepository;
        this.patientJdbcTemplateRepository = patientJdbcTemplateRepository;
        this.zonedTime = zonedTime;
        this.userService = userService;
        this.patientMapper = patientMapper;
    }

    @Transactional
    public void save(CreatePatientUserDTO patient) {
        userService.saveUser(patient);
        PatientEntity patientEntity = patientMapper.toEntity(patient);
        patientRepository.save(patientEntity);
    }

    public PatientEntity findById(Integer id) {
        return patientRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public boolean patientHasAppointmentOnSelectedTime(AppointmentEntity appointmentEntity) {
        return !patientJdbcTemplateRepository.patientDoNotHaveAppointment(
            appointmentEntity.getPatientId(), appointmentEntity.getDateTime()
        );
    }

    public boolean patientHasAppointmentWithDoctorThatDay(AppointmentEntity appointmentEntity) {
        return patientJdbcTemplateRepository.patientHasAppointmentWithThatDoctorThatDay(
            appointmentEntity.getPatientId(),
            appointmentEntity.getDoctorId(),
            appointmentEntity.getDateTime()
        );
    }

    public List<AppointmentEntity> getPendingAppointments(Integer patientId) {
        LocalDateTime dateTimeNow = zonedTime.now();
        return patientJdbcTemplateRepository.getAppointments(patientId).stream()
            .filter(appointment -> appointment.getDateTime().isAfter(dateTimeNow))
            .toList();
    }

    public List<AppointmentEntity> getArchivedAppointments(Integer patientId) {
        LocalDateTime dateTimeNow = zonedTime.now();
        return patientJdbcTemplateRepository.getAppointments(patientId).stream()
            .filter(appointment -> appointment.getDateTime().isBefore(dateTimeNow))
            .toList();
    }

}
