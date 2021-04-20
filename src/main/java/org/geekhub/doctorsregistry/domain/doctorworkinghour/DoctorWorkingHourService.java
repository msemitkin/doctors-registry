package org.geekhub.doctorsregistry.domain.doctorworkinghour;

import org.geekhub.doctorsregistry.domain.appointment.appointmenttime.AppointmentTime;
import org.geekhub.doctorsregistry.repository.doctor.DoctorRepository;
import org.geekhub.doctorsregistry.repository.doctorworkinghour.DoctorWorkingHourEntity;
import org.geekhub.doctorsregistry.repository.doctorworkinghour.DoctorWorkingHourRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class DoctorWorkingHourService {

    private final DoctorWorkingHourRepository doctorWorkingHourRepository;
    private final AppointmentTime appointmentTime;

    public DoctorWorkingHourService(
        DoctorWorkingHourRepository doctorWorkingHourRepository,
        AppointmentTime appointmentTime
    ) {
        this.doctorWorkingHourRepository = doctorWorkingHourRepository;
        this.appointmentTime = appointmentTime;
    }

    public void setWorkingHours(List<DoctorWorkingHourEntity> workingHours) {
        workingHours.forEach(doctorWorkingHourEntity -> Assert.isTrue(
            appointmentTime.isTimeValid(doctorWorkingHourEntity.getTime().toLocalTime()),
            "Time is invalid"
            )
        );
        doctorWorkingHourRepository.setDoctorWorkingHours(workingHours);
    }
}
