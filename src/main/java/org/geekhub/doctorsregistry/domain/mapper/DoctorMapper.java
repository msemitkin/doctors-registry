package org.geekhub.doctorsregistry.domain.mapper;

import org.geekhub.doctorsregistry.repository.specialization.SpecializationEntity;
import org.geekhub.doctorsregistry.web.dto.doctor.DoctorDTO;
import org.geekhub.doctorsregistry.web.api.specialization.SpecializationMapper;
import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorDTO;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    private final SpecializationMapper specializationMapper;

    public DoctorMapper(SpecializationMapper specializationMapper) {
        this.specializationMapper = specializationMapper;
    }

    public DoctorEntity toEntity(CreateDoctorDTO doctorDTO) {
        SpecializationEntity specialization = new SpecializationEntity(doctorDTO.getSpecializationId(), null);
        return new DoctorEntity(
            null,
            doctorDTO.getFirstName(),
            doctorDTO.getLastName(),
            specialization,
            doctorDTO.getClinicId(),
            doctorDTO.getPrice()
        );
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

}
