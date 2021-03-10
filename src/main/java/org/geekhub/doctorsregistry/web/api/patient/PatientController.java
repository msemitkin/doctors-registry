package org.geekhub.doctorsregistry.web.api.patient;

import org.geekhub.doctorsregistry.domain.patient.PatientService;
import org.geekhub.doctorsregistry.repository.patient.PatientEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatientController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    public PatientController(PatientService patientService, PatientMapper patientMapper) {
        this.patientService = patientService;
        this.patientMapper = patientMapper;
    }

    @GetMapping("/api/patients/{id}")
    public PatientDTO getPatientById(@PathVariable("id") Integer id) {
        PatientEntity found = patientService.findById(id);
        return patientMapper.toDTO(found);
    }

    @PostMapping("/api/patients/")
    public PatientDTO newPatient(String firstName, String lastName) {
        PatientEntity patientEntity = new PatientEntity(null, firstName, lastName);
        return patientMapper.toDTO(patientService.save(patientEntity));
    }
}
