package org.geekhub.doctorsregistry.domain.patient;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.domain.datime.ZonedTime;
import org.geekhub.doctorsregistry.domain.role.Role;
import org.geekhub.doctorsregistry.domain.user.User;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.repository.patient.PatientEntity;
import org.geekhub.doctorsregistry.repository.patient.PatientJdbcTemplateRepository;
import org.geekhub.doctorsregistry.repository.patient.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientJdbcTemplateRepository patientJdbcTemplateRepository;
    private final ZonedTime zonedTime;
    private final UserService userService;

    public PatientService(
        PatientRepository patientRepository,
        PatientJdbcTemplateRepository patientJdbcTemplateRepository,
        ZonedTime zonedTime,
        UserService userService
    ) {
        this.patientRepository = patientRepository;
        this.patientJdbcTemplateRepository = patientJdbcTemplateRepository;
        this.zonedTime = zonedTime;
        this.userService = userService;
    }

    @Transactional
    public void save(CreatePatientCommand createPatientCommand) {
        User user = toUser(createPatientCommand);
        userService.saveUser(user);

        PatientEntity patientEntity = toPatientEntity(createPatientCommand);
        patientRepository.save(patientEntity);
    }

    public PatientEntity findById(Integer id) {
        return patientRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public int getIdByEmail(String email) {
        return patientRepository.getIdByEmail(email);
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
            .collect(Collectors.toList());
    }

    public List<AppointmentEntity> getArchivedAppointments(Integer patientId) {
        LocalDateTime dateTimeNow = zonedTime.now();
        return patientJdbcTemplateRepository.getAppointments(patientId).stream()
            .filter(appointment -> appointment.getDateTime().isBefore(dateTimeNow))
            .collect(Collectors.toList());
    }

    private User toUser(CreatePatientCommand createPatientCommand) {
        return new User(
            createPatientCommand.getEmail(),
            createPatientCommand.getPassword(),
            createPatientCommand.getPasswordConfirmation(),
            Role.PATIENT
        );
    }

    private PatientEntity toPatientEntity(CreatePatientCommand createPatientCommand) {
        return new PatientEntity(
            null,
            createPatientCommand.getFirstName(),
            createPatientCommand.getLastName(),
            createPatientCommand.getEmail()
        );
    }

}
