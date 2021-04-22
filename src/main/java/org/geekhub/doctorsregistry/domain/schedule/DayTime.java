package org.geekhub.doctorsregistry.domain.schedule;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

public class DayTime {

    private final DayOfWeek day;
    private final LocalTime time;

    public DayTime(DayOfWeek day, LocalTime time) {
        this.day = day;
        this.time = time;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayTime dayTime = (DayTime) o;
        return day == dayTime.day && Objects.equals(time, dayTime.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, time);
    }

    @Override
    public String toString() {
        return "DayTime{" +
               "day=" + day +
               ", time=" + time +
               '}';
    }
}
