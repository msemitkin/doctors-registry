package org.geekhub.doctorsregistry.domain.mapper;

import org.geekhub.doctorsregistry.domain.doctor.CreateDoctorCommand;
import org.geekhub.doctorsregistry.domain.schedule.DayTimeSpliterator;
import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.geekhub.doctorsregistry.web.dto.doctor.DoctorDTO;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class DoctorMapper {

    private final SpecializationMapper specializationMapper;
    private final DayTimeSpliterator dayTimeSpliterator;

    public DoctorMapper(
        SpecializationMapper specializationMapper,
        DayTimeSpliterator dayTimeSpliterator
    ) {
        this.specializationMapper = specializationMapper;
        this.dayTimeSpliterator = dayTimeSpliterator;
    }

    public CreateDoctorCommand toCreateDoctorCommand(CreateDoctorUserDTO doctorDTO, int currentClinicId) {
        return new CreateDoctorCommand(
            doctorDTO.getFirstName(),
            doctorDTO.getLastName(),
            doctorDTO.getEmail(),
            doctorDTO.getPassword(),
            doctorDTO.getSpecializationId(),
            currentClinicId,
            doctorDTO.getPrice(),
            new HashSet<>(dayTimeSpliterator.splitToDayTime(doctorDTO.getTimetable()))
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
