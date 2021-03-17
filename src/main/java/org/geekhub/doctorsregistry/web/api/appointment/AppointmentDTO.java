package org.geekhub.doctorsregistry.web.api.appointment;

import java.time.LocalDateTime;

public class AppointmentDTO {
    private Integer id;
    private Integer pateintId;
    private Integer doctorId;
    private LocalDateTime dateTime;

    public AppointmentDTO(
        Integer id,
        Integer pateintId,
        Integer doctorId,
        LocalDateTime dateTime
    ) {
        this.id = id;
        this.pateintId = pateintId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPateintId() {
        return pateintId;
    }

    public void setPateintId(Integer pateintId) {
        this.pateintId = pateintId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
