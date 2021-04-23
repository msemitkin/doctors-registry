package org.geekhub.doctorsregistry.web.api.patient;

import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.domain.mapper.PatientMapper;
import org.geekhub.doctorsregistry.domain.patient.PatientService;
import org.geekhub.doctorsregistry.repository.patient.PatientEntity;
import org.geekhub.doctorsregistry.web.dto.appointment.AppointmentDTO;
import org.geekhub.doctorsregistry.web.dto.patient.CreatePatientUserDTO;
import org.geekhub.doctorsregistry.web.dto.patient.PatientDTO;
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

    public PatientController(
        PatientService patientService,
        PatientMapper patientMapper,
        AppointmentMapper appointmentMapper
    ) {
        this.patientService = patientService;
        this.patientMapper = patientMapper;
        this.appointmentMapper = appointmentMapper;
    }

    @GetMapping("/api/patients/{id}")
    public PatientDTO getPatientById(@PathVariable("id") Integer id) {
        PatientEntity found = patientService.findById(id);
        return patientMapper.toDTO(found);
    }

    @GetMapping("/api/patients/{id}/appointments/pending")
    public List<AppointmentDTO> getPendingAppointments(@PathVariable("id") Integer patientId) {
        return patientService.getPendingAppointments(patientId).stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/api/patients/{id}/appointments/archive")
    public List<AppointmentDTO> getArchivedAppointments(@PathVariable("id") Integer patientId) {
        return patientService.getArchivedAppointments(patientId).stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList());
    }

    @PostMapping("/api/patients/")
    @ResponseStatus(HttpStatus.CREATED)
    public void newPatient(@Valid CreatePatientUserDTO patientDTO) {
        patientService.save(patientDTO);
    }
}
