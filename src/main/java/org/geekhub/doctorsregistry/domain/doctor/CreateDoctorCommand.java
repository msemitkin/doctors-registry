package org.geekhub.doctorsregistry.domain.doctor;

import org.geekhub.doctorsregistry.domain.schedule.DayTime;
import org.springframework.lang.NonNull;

import java.util.Set;

public record CreateDoctorCommand(
    @NonNull String firstName,
    @NonNull String lastName,
    @NonNull String email,
    @NonNull String password,
    int specialization,
    int clinicId,
    int price,
    Set<DayTime> timetable
) {
}
