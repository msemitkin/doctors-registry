package org.geekhub.doctorsregistry.domain;

import org.geekhub.doctorsregistry.domain.appointment.appointmenttime.AppointmentTime;
import org.geekhub.doctorsregistry.repository.doctorworkinghour.DoctorWorkingHourEntity;
import org.geekhub.doctorsregistry.repository.doctorworkinghour.DoctorWorkingHourRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class DoctorWorkingHourService {

    private final DoctorWorkingHourRepository doctorWorkingHourRepository;

    public DoctorWorkingHourService(DoctorWorkingHourRepository doctorWorkingHourRepository) {
        this.doctorWorkingHourRepository = doctorWorkingHourRepository;
    }

    public void addWorkingHour(DoctorWorkingHourEntity doctorWorkingHour) {
        Assert.isTrue(
            AppointmentTime.isTimeValid(doctorWorkingHour.getTime().toLocalTime()),
            "Time is invalid"
        );

        doctorWorkingHourRepository.add(doctorWorkingHour);
    }
}
