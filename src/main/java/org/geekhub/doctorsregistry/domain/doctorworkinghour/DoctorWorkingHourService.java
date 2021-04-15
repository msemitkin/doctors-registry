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
    private final DoctorRepository doctorRepository;

    public DoctorWorkingHourService(
        DoctorWorkingHourRepository doctorWorkingHourRepository,
        DoctorRepository doctorRepository
    ) {
        this.doctorWorkingHourRepository = doctorWorkingHourRepository;
        this.doctorRepository = doctorRepository;
    }

    public void addWorkingHour(DoctorWorkingHourEntity doctorWorkingHour) {

        Assert.isTrue(
            AppointmentTime.isTimeValid(doctorWorkingHour.getTime().toLocalTime()),
            "Time is invalid"
        );
        Assert.isTrue(
            doctorRepository.existsById(doctorWorkingHour.getDoctorId()),
            () -> "Doctor with given id does not exist: " + doctorWorkingHour.getDoctorId()
        );

        doctorWorkingHourRepository.add(doctorWorkingHour);
    }

    public void setWorkingHours(List<DoctorWorkingHourEntity> workingHours) {
        doctorWorkingHourRepository.setDoctorWorkingHours(workingHours);
    }
}
