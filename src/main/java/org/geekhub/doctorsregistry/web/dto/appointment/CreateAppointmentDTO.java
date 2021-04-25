package org.geekhub.doctorsregistry.web.dto.appointment;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class CreateAppointmentDTO {

    @NotNull(message = "Doctor id must be specified")
    private Integer doctorId;
    @NotNull(message = "Appointment date and time must be specified")
    private String inputDateTime;

    public CreateAppointmentDTO(Integer doctorId, String inputDateTime) {
        this.doctorId = doctorId;
        this.inputDateTime = inputDateTime;
    }

    public CreateAppointmentDTO() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateAppointmentDTO that = (CreateAppointmentDTO) o;
        return Objects.equals(doctorId, that.doctorId) && Objects.equals(inputDateTime, that.inputDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorId, inputDateTime);
    }

    @Override
    public String toString() {
        return "CreateAppointmentDTO{" +
               "doctorId=" + doctorId +
               ", inputDateTime='" + inputDateTime + '\'' +
               '}';
    }
}
