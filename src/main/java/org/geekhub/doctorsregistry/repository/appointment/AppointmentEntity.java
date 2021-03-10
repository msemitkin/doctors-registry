package org.geekhub.doctorsregistry.repository.appointment;

import org.geekhub.doctorsregistry.repository.doctorworkinghour.DoctorWorkingHour;
import org.geekhub.doctorsregistry.repository.patient.PatientEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;
    @ManyToOne
    @JoinColumn(name = "doctor_working_hour_id")
    private DoctorWorkingHour doctorWorkingHour;
    @Column(name = "datetime")
    private LocalDateTime dateTime;

    protected AppointmentEntity() {
    }

    public AppointmentEntity(Integer id, PatientEntity patient, DoctorWorkingHour doctorWorkingHour, LocalDateTime dateTime) {
        this.id = id;
        this.patient = patient;
        this.doctorWorkingHour = doctorWorkingHour;
        this.dateTime = dateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PatientEntity getPatient() {
        return patient;
    }

    public void setPatient(PatientEntity patient) {
        this.patient = patient;
    }

    public DoctorWorkingHour getDoctorWorkingHour() {
        return doctorWorkingHour;
    }

    public void setDoctorWorkingHour(DoctorWorkingHour doctorWorkingHour) {
        this.doctorWorkingHour = doctorWorkingHour;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
