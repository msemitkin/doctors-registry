package org.geekhub.doctorsregistry.api.patient;

import org.geekhub.doctorsregistry.api.dto.appointment.AppointmentDTO;
import org.geekhub.doctorsregistry.api.dto.patient.CreatePatientUserDTO;
import org.geekhub.doctorsregistry.api.dto.patient.PatientDTO;
import org.geekhub.doctorsregistry.api.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.api.mapper.PatientMapper;
import org.geekhub.doctorsregistry.domain.AuthenticationResolver;
import org.geekhub.doctorsregistry.domain.patient.CreatePatientCommand;
import org.geekhub.doctorsregistry.domain.patient.PatientService;
import org.geekhub.doctorsregistry.repository.patient.PatientEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PatientController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final AppointmentMapper appointmentMapper;
    private final AuthenticationResolver authenticationResolver;

    public PatientController(
        PatientService patientService,
        PatientMapper patientMapper,
        AppointmentMapper appointmentMapper,
        AuthenticationResolver authenticationResolver
    ) {
        this.patientService = patientService;
        this.patientMapper = patientMapper;
        this.appointmentMapper = appointmentMapper;
        this.authenticationResolver = authenticationResolver;
    }

    @GetMapping("/api/patients/{id}")
    public PatientDTO getPatientById(@PathVariable("id") Integer id) {
        PatientEntity found = patientService.findById(id);
        return patientMapper.toDTO(found);
    }

    @GetMapping("/api/patients/me/appointments/pending")
    public List<AppointmentDTO> getPendingAppointments() {
        int patientId = authenticationResolver.getUserId();
        return patientService.getPendingAppointments(patientId).stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/api/patients/me/appointments/archive")
    public List<AppointmentDTO> getArchivedAppointments() {
        int patientId = authenticationResolver.getUserId();
        return patientService.getArchivedAppointments(patientId).stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList());
    }

    @PostMapping("/api/patients")
    @ResponseStatus(HttpStatus.CREATED)
    public void newPatient(@Valid CreatePatientUserDTO patientDTO) {
        CreatePatientCommand createPatientCommand = toCreatePatientCommand(patientDTO);
        patientService.save(createPatientCommand);
    }

    private CreatePatientCommand toCreatePatientCommand(CreatePatientUserDTO patientDTO) {
        return new CreatePatientCommand(
            patientDTO.getFirstName(),
            patientDTO.getLastName(),
            patientDTO.getEmail(),
            patientDTO.getPassword(),
            patientDTO.getPasswordConfirmation()
        );
    }
}
