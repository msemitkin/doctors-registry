package org.geekhub.doctorsregistry.domain.appointment;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class CreateAppointmentCommand {

    private final int patientId;
    private final int doctorId;
    @NonNull
    private final LocalDateTime appointmentTime;

    public CreateAppointmentCommand(int patientId, int doctorId, @NonNull LocalDateTime appointmentTime) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentTime = Objects.requireNonNull(appointmentTime);
    }

    public int getPatientId() {
        return patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    @NonNull
    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }
}
