package org.geekhub.doctorsregistry.web.api;

import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.geekhub.doctorsregistry.repository.specialization.SpecializationEntity;
import org.geekhub.doctorsregistry.web.doctor.DoctorDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("api/doctors")
    public DoctorDTO saveDoctor(
        String firstName,
        String lastName,
        Integer specializationId,
        Integer clinicId,
        Integer price
    ) {
        SpecializationEntity specialization
            = new SpecializationEntity(specializationId, null);
        DoctorEntity doctor
            = new DoctorEntity(null, firstName, lastName, specialization, clinicId, price);
        DoctorEntity saved = doctorService.save(doctor);
        return DoctorDTO.of(saved);
    }

    @GetMapping("api/doctors")
    public List<DoctorDTO> getAllDoctors() {
        return doctorService.findAll().stream()
            .map(DoctorDTO::of)
            .collect(Collectors.toList());
    }

    @GetMapping("api/doctors/{id}")
    public DoctorDTO getDoctorById(@PathVariable("id") Integer id) {
        DoctorEntity found = doctorService.findById(id);
        return DoctorDTO.of(found);
    }

    @DeleteMapping("api/doctors/{id}")
    public void deleteDoctorById(@PathVariable("id") Integer id) {
        doctorService.deleteById(id);
    }

}
