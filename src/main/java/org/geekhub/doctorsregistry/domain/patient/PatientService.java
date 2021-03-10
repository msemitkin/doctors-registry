package org.geekhub.doctorsregistry.domain.patient;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.repository.patient.PatientEntity;
import org.geekhub.doctorsregistry.repository.patient.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public PatientEntity save(PatientEntity patientEntity) {
        return patientRepository.save(patientEntity);
    }

    public PatientEntity findById(Integer id) {
        return patientRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public void deleteById(Integer id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id);
        }
    }
}
