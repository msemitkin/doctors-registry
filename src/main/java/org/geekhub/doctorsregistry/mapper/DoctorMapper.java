package org.geekhub.doctorsregistry.mapper;

import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.geekhub.doctorsregistry.web.doctor.DoctorDTO;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    private final SpecializationMapper specializationMapper;

    public DoctorMapper(SpecializationMapper specializationMapper) {
        this.specializationMapper = specializationMapper;
    }

    public DoctorDTO toDTO(DoctorEntity entity) {
        return new DoctorDTO(
            entity.getId(),
            entity.getFirstName(),
            entity.getLastName(),
            specializationMapper.toDTO(entity.getSpecialization()),
            entity.getClinicId(),
            entity.getPrice()
        );
    }

    public DoctorEntity toEntity(DoctorDTO doctorDTO) {
        return new DoctorEntity(
            doctorDTO.id(),
            doctorDTO.getFirstName(),
            doctorDTO.getLastName(),
            specializationMapper.toEntity(doctorDTO.specialization()),
            doctorDTO.clinicId(),
            doctorDTO.price()
        );
    }

}
