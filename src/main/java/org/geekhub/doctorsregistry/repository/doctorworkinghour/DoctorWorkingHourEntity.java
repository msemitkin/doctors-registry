package org.geekhub.doctorsregistry.repository.doctorworkinghour;

import java.sql.Time;

public class DoctorWorkingHourEntity {

    private Integer id;
    private Integer doctorId;
    private Time time;
    private Integer dayOfTheWeek;

    protected DoctorWorkingHourEntity() {
    }

    public DoctorWorkingHourEntity(Integer id, Integer doctorId, Time time, Integer dayOfTheWeek) {
        this.id = id;
        this.doctorId = doctorId;
        this.time = time;
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Integer getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(Integer dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }
}
