package org.geekhub.doctorsregistry.web.dto.appointment;

public class CreateAppointmentDTO {

    private Integer patientId;
    private Integer doctorId;
    private String inputDateTime;

    protected CreateAppointmentDTO() {
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

    public String getInputDateTime() {
        return inputDateTime;
    }

    public void setInputDateTime(String inputDateTime) {
        this.inputDateTime = inputDateTime;
    }
}
