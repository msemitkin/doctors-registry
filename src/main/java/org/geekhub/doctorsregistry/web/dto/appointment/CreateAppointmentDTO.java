package org.geekhub.doctorsregistry.web.dto.appointment;

public class CreateAppointmentDTO {

    private Integer doctorId;
    private String inputDateTime;

    public CreateAppointmentDTO(Integer doctorId, String inputDateTime) {
        this.doctorId = doctorId;
        this.inputDateTime = inputDateTime;
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
