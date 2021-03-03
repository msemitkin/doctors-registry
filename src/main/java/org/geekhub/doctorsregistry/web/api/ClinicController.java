package org.geekhub.doctorsregistry.web.api;

import org.geekhub.doctorsregistry.domain.ClinicService;
import org.geekhub.doctorsregistry.repository.clinic.ClinicEntity;
import org.geekhub.doctorsregistry.web.clinic.ClinicDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ClinicController {

    private final ClinicService clinicService;

    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @PostMapping("/api/clinics")
    public ClinicDTO saveClinic(ClinicDTO clinicDTO) {
        ClinicEntity saved = clinicService.save(ClinicEntity.of(clinicDTO));
        return ClinicDTO.of(saved);
    }

    @GetMapping("/api/clinics")
    public List<ClinicDTO> findAllClinics() {
        return clinicService.findAll().stream()
            .map(ClinicDTO::of)
            .collect(Collectors.toList());
    }

    @GetMapping("/api/clinics/{id}")
    public ClinicDTO findClinicById(@PathVariable("id") Integer id) {
        ClinicEntity found = clinicService.findById(id);
        return ClinicDTO.of(found);
    }

    @DeleteMapping("/api/clinics/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteClinicById(@PathVariable("id") Integer id) {
        clinicService.deleteById(id);
    }
}
