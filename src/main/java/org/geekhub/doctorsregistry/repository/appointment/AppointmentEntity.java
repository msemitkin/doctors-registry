package org.geekhub.doctorsregistry.repository.appointment;

import java.time.LocalDateTime;
import java.util.Objects;

public class AppointmentEntity {

    private Integer id;
    private Integer patientId;
    private Integer doctorId;
    private LocalDateTime dateTime;

    protected AppointmentEntity() {
    }

    public AppointmentEntity(
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

    public static AppointmentEntity of(Integer patientId, Integer doctorId, LocalDateTime dateTime) {
        return new AppointmentEntity(null, patientId, doctorId, dateTime);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentEntity that = (AppointmentEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(patientId, that.patientId) && Objects.equals(doctorId, that.doctorId) && Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientId, doctorId, dateTime);
    }

    @Override
    public String toString() {
        return "AppointmentEntity{" +
               "id=" + id +
               ", patientId=" + patientId +
               ", doctorId=" + doctorId +
               ", dateTime=" + dateTime +
               '}';
    }
}
