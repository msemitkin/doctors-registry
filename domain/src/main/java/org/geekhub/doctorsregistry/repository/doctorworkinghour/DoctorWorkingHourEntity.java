package org.geekhub.doctorsregistry.repository.doctorworkinghour;

import java.sql.Time;

public class DoctorWorkingHourEntity {

    private final Integer id;
    private final Integer doctorId;
    private final Time time;
    private final Integer dayOfTheWeek;

    public DoctorWorkingHourEntity(Integer id, Integer doctorId, Time time, Integer dayOfTheWeek) {
        this.id = id;
        this.doctorId = doctorId;
        this.time = time;
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public Integer getId() {
        return id;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public Time getTime() {
        return time;
    }

    public Integer getDayOfTheWeek() {
        return dayOfTheWeek;
    }
}
