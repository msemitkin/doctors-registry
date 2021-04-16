package org.geekhub.doctorsregistry.web.dto.doctorworkinghour;

import java.time.LocalTime;

public class DoctorWorkingHourDTO {

    private final Integer doctorId;
    private final LocalTime time;
    private final String dayOfWeek;

    public DoctorWorkingHourDTO(Integer doctorId, LocalTime time, String dayOfWeek) {
        this.doctorId = doctorId;
        this.time = time;
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }
}
