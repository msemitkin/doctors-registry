package org.geekhub.doctorsregistry.domain.clinic;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.repository.clinic.ClinicEntity;
import org.geekhub.doctorsregistry.repository.clinic.ClinicRepository;
import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ClinicService {

    private final ClinicRepository clinicRepository;
    private final DoctorService doctorService;

    public ClinicService(ClinicRepository clinicRepository, DoctorService doctorService) {
        this.clinicRepository = clinicRepository;
        this.doctorService = doctorService;
    }

    public ClinicEntity save(ClinicEntity clinicEntity) {
        return clinicRepository.save(clinicEntity);
    }

    public List<ClinicEntity> findAll() {
        return StreamSupport.stream(clinicRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }

    public ClinicEntity findById(int id) {
        return clinicRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }


    public List<DoctorEntity> getDoctors(Integer clinicId) {
        return doctorService.findDoctorsByClinic(clinicId);
    }

    @Transactional
    public void deleteById(int id) {
        doctorService.deleteDoctorsByClinicId(id);
        if (clinicRepository.existsById(id)) {
            clinicRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id);
        }
    }

}
