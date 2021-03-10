package org.geekhub.doctorsregistry.web.appointment;

import org.geekhub.doctorsregistry.web.doctor.DoctorDTO;
import org.geekhub.doctorsregistry.web.patient.PatientDTO;

import java.time.LocalDateTime;

public class AppointmentDTO {
    private Integer id;
    private PatientDTO patientDTO;
    private DoctorDTO doctorDTO;
    private LocalDateTime dateTime;

    public AppointmentDTO(Integer id, PatientDTO patientDTO, DoctorDTO doctorDTO, LocalDateTime dateTime) {
        this.id = id;
        this.patientDTO = patientDTO;
        this.doctorDTO = doctorDTO;
        this.dateTime = dateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PatientDTO getPatientDTO() {
        return patientDTO;
    }

    public void setPatientDTO(PatientDTO patientDTO) {
        this.patientDTO = patientDTO;
    }

    public DoctorDTO getDoctorDTO() {
        return doctorDTO;
    }

    public void setDoctorDTO(DoctorDTO doctorDTO) {
        this.doctorDTO = doctorDTO;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
