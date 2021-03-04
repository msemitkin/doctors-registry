package org.geekhub.doctorsregistry.domain.doctor;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.geekhub.doctorsregistry.repository.doctor.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public DoctorEntity save(DoctorEntity doctorEntity) {
        return doctorRepository.save(doctorEntity);
    }

    public List<DoctorEntity> findAll() {
        return StreamSupport.stream(doctorRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }

    public DoctorEntity findById(int id) {
        return doctorRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

    public List<DoctorEntity> findDoctorsByClinic(Integer clinicId) {
        return doctorRepository.findDoctorEntitiesByClinicId(clinicId);
    }

    public void deleteById(int id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id);
        }
    }

    public void deleteDoctorsByClinicId(Integer clinicId) {
        doctorRepository.deleteDoctorEntitiesByClinicId(clinicId);
    }

}
