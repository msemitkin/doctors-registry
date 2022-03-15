package org.geekhub.doctorsregistry.repository.appointment;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class AppointmentEntity {
    private final Integer id;
    private final int patientId;
    private final int doctorId;
    @NonNull
    private final LocalDateTime dateTime;

    private AppointmentEntity(
        Integer id,
        int patientId,
        int doctorId,
        @NonNull LocalDateTime dateTime
    ) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = Objects.requireNonNull(dateTime);
    }

    public static AppointmentEntity create(
        int patientId,
        int doctorId,
        @NonNull LocalDateTime dateTime
    ) {
        return new AppointmentEntity(null, patientId, doctorId, dateTime);
    }

    public static AppointmentEntity withId(
        int id,
        int patientId,
        int doctorId,
        @NonNull LocalDateTime dateTime
    ) {
        return new AppointmentEntity(id, patientId, doctorId, dateTime);
    }

    public Integer getId() {
        return id;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    @NonNull
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentEntity that = (AppointmentEntity) o;
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
        return "AppointmentEntity{" +
            "id=" + id +
            ", patientId=" + patientId +
            ", doctorId=" + doctorId +
            ", dateTime=" + dateTime +
            '}';
    }
}
