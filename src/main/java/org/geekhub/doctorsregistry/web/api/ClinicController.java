package org.geekhub.doctorsregistry.web.api;

import org.geekhub.doctorsregistry.domain.clinic.ClinicService;
import org.geekhub.doctorsregistry.mapper.ClinicMapper;
import org.geekhub.doctorsregistry.mapper.DoctorMapper;
import org.geekhub.doctorsregistry.repository.clinic.ClinicEntity;
import org.geekhub.doctorsregistry.web.clinic.ClinicDTO;
import org.geekhub.doctorsregistry.web.doctor.DoctorDTO;
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
    private final DoctorMapper doctorMapper;

    public ClinicController(
        ClinicService clinicService,
        ClinicMapper clinicMapper,
        DoctorMapper doctorMapper
    ) {
        this.clinicService = clinicService;
        this.clinicMapper = clinicMapper;
        this.doctorMapper = doctorMapper;
    }

    @PostMapping("/api/clinics")
    public ClinicDTO saveClinic(String name, String address) {
        ClinicEntity clinicEntity = new ClinicEntity(null, name, address);
        ClinicEntity saved = clinicService.save(clinicEntity);
        return clinicMapper.toDTO(saved);
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

    @GetMapping("/api/clinics/{id}/doctors")
    public List<DoctorDTO> getDoctorsByClinicId(@PathVariable("id") Integer id) {
        return clinicService.getDoctors(id).stream()
            .map(doctorMapper::toDTO)
            .collect(Collectors.toList());
    }

}
