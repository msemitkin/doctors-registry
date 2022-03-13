package org.geekhub.doctorsregistry.web.api.patient;

import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.domain.mapper.PatientMapper;
import org.geekhub.doctorsregistry.domain.patient.PatientService;
import org.geekhub.doctorsregistry.repository.patient.PatientEntity;
import org.geekhub.doctorsregistry.web.dto.appointment.AppointmentDTO;
import org.geekhub.doctorsregistry.web.dto.patient.CreatePatientUserDTO;
import org.geekhub.doctorsregistry.web.dto.patient.PatientDTO;
import org.geekhub.doctorsregistry.web.security.AuthenticationPrincipalExtractor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PatientController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final AppointmentMapper appointmentMapper;
    private final AuthenticationPrincipalExtractor authenticationPrincipalExtractor;

    public PatientController(
        PatientService patientService,
        PatientMapper patientMapper,
        AppointmentMapper appointmentMapper,
        AuthenticationPrincipalExtractor authenticationPrincipalExtractor
    ) {
        this.patientService = patientService;
        this.patientMapper = patientMapper;
        this.appointmentMapper = appointmentMapper;
        this.authenticationPrincipalExtractor = authenticationPrincipalExtractor;
    }

    @GetMapping("/api/patients/{id}")
    public PatientDTO getPatientById(@PathVariable("id") Integer id) {
        PatientEntity found = patientService.findById(id);
        return patientMapper.toDTO(found);
    }

    @GetMapping("/api/patients/me/appointments/pending")
    public List<AppointmentDTO> getPendingAppointments() {
        int patientId = authenticationPrincipalExtractor.getPrincipal().userId();
        return patientService.getPendingAppointments(patientId).stream()
            .map(appointmentMapper::toDTO)
            .toList();
    }

    @GetMapping("/api/patients/me/appointments/archive")
    public List<AppointmentDTO> getArchivedAppointments() {
        int id = authenticationPrincipalExtractor.getPrincipal().userId();
        return patientService.getArchivedAppointments(id).stream()
            .map(appointmentMapper::toDTO)
            .toList();
    }

    @PostMapping("/api/patients")
    @ResponseStatus(HttpStatus.CREATED)
    public void newPatient(@Valid CreatePatientUserDTO patientDTO) {
        patientService.save(patientDTO);
    }
}
