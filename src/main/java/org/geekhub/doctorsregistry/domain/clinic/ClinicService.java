package org.geekhub.doctorsregistry.domain.clinic;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.repository.clinic.ClinicEntity;
import org.geekhub.doctorsregistry.repository.clinic.ClinicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ClinicService {

    private final ClinicRepository clinicRepository;

    public ClinicService(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
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

    public void deleteById(int id) {
        if (clinicRepository.existsById(id)) {
            clinicRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id);
        }
    }

}
