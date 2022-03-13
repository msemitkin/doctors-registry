package org.geekhub.doctorsregistry.domain.appointment.appointmenttime;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AppointmentTime {

    private final Set<LocalTime> availableTime = init();
    private static final LocalTime WORKING_DAY_STARTS_AT = LocalTime.parse("08:00");
    private static final LocalTime WORKING_DAY_ENDS_AT = LocalTime.parse("20:00");
    private static final int MINUTES_FREQUENCY = 20;

    private Set<LocalTime> init() {
        Set<LocalTime> times = new HashSet<>();
        LocalTime time = WORKING_DAY_STARTS_AT;
        while (true) {
            LocalTime appointmentEndsAt = time.plusMinutes(MINUTES_FREQUENCY);
            if (appointmentEndsAt.isAfter(WORKING_DAY_ENDS_AT)) {
                break;
            }
            times.add(time);
            time = time.plusMinutes(MINUTES_FREQUENCY);
        }
        return times;
    }

    public boolean isTimeValid(LocalTime time) {
        return availableTime.contains(time);
    }

    public List<LocalTime> getSupportedTimes() {
        return new ArrayList<>(availableTime).stream()
            .sorted().toList();
    }

}
