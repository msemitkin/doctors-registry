package org.geekhub.doctorsregistry.domain.appointment.appointmenttime;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppointmentTime {

    private final Set<LocalTime> availableTime = new HashSet<>();

    @PostConstruct
    public void init() {
        for (int hour = 8; hour < 20; hour++) {
            for (int minute : new int[]{0, 20, 40}) {
                availableTime.add(LocalTime.of(hour, minute));
            }
        }
    }

    public boolean isTimeValid(LocalTime time) {
        return availableTime.contains(time);
    }

    public List<LocalTime> getSupportedTimes() {
        return new ArrayList<>(availableTime).stream()
            .sorted()
            .collect(Collectors.toList());
    }

}
