package org.geekhub.doctorsregistry.domain.appointment;

import java.time.LocalDateTime;

public class CreateAppointmentCommand {

    private final int patientId;
    private final int doctorId;
    private final LocalDateTime appointmentTime;

    public CreateAppointmentCommand(int patientId, int doctorId, LocalDateTime appointmentTime) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentTime = appointmentTime;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }
}
