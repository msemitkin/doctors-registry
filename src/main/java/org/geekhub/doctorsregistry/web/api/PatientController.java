package org.geekhub.doctorsregistry.web.api;

import org.geekhub.doctorsregistry.domain.patient.PatientService;
import org.geekhub.doctorsregistry.repository.patient.PatientEntity;
import org.geekhub.doctorsregistry.web.patient.PatientDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/api/patients/{id}")
    public PatientDTO getPatientById(@PathVariable("id") Integer id) {
        PatientEntity found = patientService.findById(id);
        return PatientDTO.of(found);
    }

    @PostMapping("/api/patients/")
    public PatientDTO newPatient(String firstName, String lastName) {
        PatientEntity patientEntity = new PatientEntity(null, firstName, lastName);
        return PatientDTO.of(patientService.save(patientEntity));
    }
}
