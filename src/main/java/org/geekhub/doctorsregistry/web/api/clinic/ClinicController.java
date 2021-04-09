package org.geekhub.doctorsregistry.web.api.clinic;

import org.geekhub.doctorsregistry.domain.clinic.ClinicService;
import org.geekhub.doctorsregistry.repository.clinic.ClinicEntity;
import org.geekhub.doctorsregistry.web.dto.clinic.RegisterClinicDTO;
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
    private final ClinicMapper clinicMapper;

    public ClinicController(
        ClinicService clinicService,
        ClinicMapper clinicMapper
    ) {
        this.clinicService = clinicService;
        this.clinicMapper = clinicMapper;
    }

    @PostMapping("/api/clinics")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveClinic(RegisterClinicDTO clinicDTO) {
        clinicService.save(clinicDTO);
    }

    @GetMapping("/api/clinics")
    public List<ClinicDTO> findAllClinics() {
        return clinicService.findAll().stream()
            .map(clinicMapper::toDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/api/clinics/{id}")
    public ClinicDTO findClinicById(@PathVariable("id") Integer id) {
        ClinicEntity found = clinicService.findById(id);
        return clinicMapper.toDTO(found);
    }

    @DeleteMapping("/api/clinics/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteClinicById(@PathVariable("id") Integer id) {
        clinicService.deleteById(id);
    }

}
