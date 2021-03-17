package org.geekhub.doctorsregistry.web.api.doctorworkinghour;

import org.geekhub.doctorsregistry.domain.DoctorWorkingHourService;
import org.geekhub.doctorsregistry.repository.doctorworkinghour.DoctorWorkingHourEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
public class DoctorWorkingHourController {

    private final DoctorWorkingHourService doctorWorkingHourService;
    private final DoctorWorkingHourMapper doctorWorkingHourMapper;

    public DoctorWorkingHourController(
        DoctorWorkingHourService doctorWorkingHourService,
        DoctorWorkingHourMapper doctorWorkingHourMapper
    ) {
        this.doctorWorkingHourService = doctorWorkingHourService;
        this.doctorWorkingHourMapper = doctorWorkingHourMapper;
    }

    @PostMapping("/api/doctors/{id}/working-hours")
    public void addWorkingHours(@PathVariable("id") Integer doctorId, String time, String dayOfWeek) {

        DoctorWorkingHourDTO dto = new DoctorWorkingHourDTO(doctorId, LocalTime.parse(time), dayOfWeek);
        DoctorWorkingHourEntity entity = doctorWorkingHourMapper.toEntity(dto);
        doctorWorkingHourService.addWorkingHour(entity);

    }
}
