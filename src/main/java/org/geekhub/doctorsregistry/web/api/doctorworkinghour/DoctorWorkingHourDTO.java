package org.geekhub.doctorsregistry.web.api.doctorworkinghour;

import java.time.LocalTime;

public class DoctorWorkingHourDTO {

    Integer doctorId;
    LocalTime time;
    String dayOfWeek;

    public DoctorWorkingHourDTO(Integer doctorId, LocalTime time, String dayOfWeek) {
        this.doctorId = doctorId;
        this.time = time;
        this.dayOfWeek = dayOfWeek;
    }
}
