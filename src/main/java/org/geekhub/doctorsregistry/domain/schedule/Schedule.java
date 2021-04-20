package org.geekhub.doctorsregistry.domain.schedule;

import org.geekhub.doctorsregistry.domain.appointment.appointmenttime.AppointmentTime;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class Schedule {
    private final AppointmentTime appointmentTime;

    public Schedule(AppointmentTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Map<DayOfWeek, List<LocalTime>> getScheduleMap() {
        Map<DayOfWeek, List<LocalTime>> weekSchedule = new LinkedHashMap<>();
        List<LocalTime> supportedTimes = appointmentTime.getSupportedTimes();
        DayOfWeek[] days = DayOfWeek.values();
        for (DayOfWeek day : days) {
            List<LocalTime> daySchedule = new ArrayList<>(supportedTimes);
            weekSchedule.put(day, daySchedule);
        }
        return weekSchedule;
    }
}
