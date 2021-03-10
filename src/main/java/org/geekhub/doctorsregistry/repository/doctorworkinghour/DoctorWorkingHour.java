package org.geekhub.doctorsregistry.repository.doctorworkinghour;

import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.geekhub.doctorsregistry.repository.workinghour.WorkingHourEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "doctor_working_hour")
public class DoctorWorkingHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private DoctorEntity doctorEntity;

    @ManyToOne
    @JoinColumn(name = "working_hour_id")
    private WorkingHourEntity workingHour;

    protected DoctorWorkingHour() {
    }

    public DoctorWorkingHour(Integer id, DoctorEntity doctorEntity, WorkingHourEntity workingHour) {
        this.id = id;
        this.doctorEntity = doctorEntity;
        this.workingHour = workingHour;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DoctorEntity getDoctorEntity() {
        return doctorEntity;
    }

    public void setDoctorEntity(DoctorEntity doctorEntity) {
        this.doctorEntity = doctorEntity;
    }

    public WorkingHourEntity getWorkingHour() {
        return workingHour;
    }

    public void setWorkingHour(WorkingHourEntity workingHour) {
        this.workingHour = workingHour;
    }
}
