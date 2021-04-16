package org.geekhub.doctorsregistry.web.dto.appointment;

import java.time.LocalDateTime;

public class AppointmentDTO {
    private Integer id;
    private Integer patientId;
    private Integer doctorId;
    private LocalDateTime dateTime;

    public AppointmentDTO(
        Integer id,
        Integer patientId,
        Integer doctorId,
        LocalDateTime dateTime
    ) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
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
