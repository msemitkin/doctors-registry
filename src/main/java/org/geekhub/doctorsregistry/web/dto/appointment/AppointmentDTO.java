package org.geekhub.doctorsregistry.web.dto.appointment;

import java.time.LocalDateTime;
import java.util.Objects;

public class AppointmentDTO {
    private final Integer id;
    private final Integer patientId;
    private final Integer doctorId;
    private final String dateTime;

    public AppointmentDTO(
        Integer id,
        Integer patientId,
        Integer doctorId,
        String dateTime
    ) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
    }

    public Integer getId() {
        return id;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public String getDateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentDTO that = (AppointmentDTO) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(patientId, that.patientId) &&
               Objects.equals(doctorId, that.doctorId) &&
               Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientId, doctorId, dateTime);
    }

    @Override
    public String toString() {
        return "AppointmentDTO{" +
               "id=" + id +
               ", patientId=" + patientId +
               ", doctorId=" + doctorId +
               ", dateTime='" + dateTime + '\'' +
               '}';
    }
}
