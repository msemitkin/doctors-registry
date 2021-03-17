package org.geekhub.doctorsregistry.web.api.doctorworkinghour;

import org.geekhub.doctorsregistry.repository.doctorworkinghour.DoctorWorkingHourEntity;
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
        String dayOfWeek = dto.dayOfWeek.trim().toUpperCase();
        return new DoctorWorkingHourEntity(
            null,
            dto.doctorId,
            Time.valueOf(dto.time),
            DayOfWeek.valueOf(dayOfWeek).getValue()
        );
    }
}
