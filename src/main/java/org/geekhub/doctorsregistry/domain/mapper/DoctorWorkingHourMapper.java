package org.geekhub.doctorsregistry.domain.mapper;

import org.geekhub.doctorsregistry.repository.doctorworkinghour.DoctorWorkingHourEntity;
import org.geekhub.doctorsregistry.web.dto.doctorworkinghour.DoctorWorkingHourDTO;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.DayOfWeek;

@Component
public class DoctorWorkingHourMapper {

    public DoctorWorkingHourDTO toDTO(DoctorWorkingHourEntity entity) {

        return new DoctorWorkingHourDTO(
            entity.getDoctorId(),
            entity.getTime().toLocalTime(),
            DayOfWeek.of(entity.getDayOfTheWeek()).name()
        );

    }

    public DoctorWorkingHourEntity toEntity(DoctorWorkingHourDTO dto) {
        String dayOfWeek = dto.getDayOfWeek().trim().toUpperCase();
        return new DoctorWorkingHourEntity(
            null,
            dto.getDoctorId(),
            Time.valueOf(dto.getTime()),
            DayOfWeek.valueOf(dayOfWeek).getValue()
        );
    }
}
