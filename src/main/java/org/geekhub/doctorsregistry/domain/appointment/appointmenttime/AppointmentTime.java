package org.geekhub.doctorsregistry.domain.appointment.appointmenttime;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppointmentTime {

    private static final Set<LocalTime> availableTime = new HashSet<>();

    static {
        for (int hour = 8; hour < 20; hour++) {
            for (int minute : new int[]{0, 20, 40}) {
                availableTime.add(LocalTime.of(hour, minute));
            }
        }
    }

    public static boolean isTimeValid(LocalTime time) {
        return availableTime.contains(time);
    }

    public static List<LocalTime> getSupportedTimes() {
        return new ArrayList<>(availableTime);
    }

    private AppointmentTime() {
    }
}
