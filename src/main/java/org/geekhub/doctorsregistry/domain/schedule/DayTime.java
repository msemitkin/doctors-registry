package org.geekhub.doctorsregistry.domain.schedule;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record DayTime(DayOfWeek day, LocalTime time) {
}
