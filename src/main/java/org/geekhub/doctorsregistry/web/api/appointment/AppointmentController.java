package org.geekhub.doctorsregistry.web.api.appointment;

import org.geekhub.doctorsregistry.domain.appointment.AppointmentService;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    public AppointmentController(
        AppointmentService appointmentService,
        AppointmentMapper appointmentMapper
    ) {
        this.appointmentService = appointmentService;
        this.appointmentMapper = appointmentMapper;
    }

    @PostMapping("/api/appointments")
    public void createAppointment(Integer patientId, Integer doctorId, String inputDateTime) {
        LocalDateTime localDateTime = LocalDateTime.parse(inputDateTime);
        AppointmentEntity entity = new AppointmentEntity(null, patientId, doctorId, localDateTime);
        appointmentService.create(entity);
    }

    @GetMapping("/api/appointments/{appointment-id}")
    public AppointmentDTO getAppointmentById(
        @PathVariable("appointment-id") Integer appointmentId
    ) {
        AppointmentEntity entity = appointmentService.findById(appointmentId);
        return appointmentMapper.toDTO(entity);
    }

    @GetMapping("/api/doctors/{id}/schedule")
    public Map<LocalDate, List<LocalTime>> getSchedule(
        @PathVariable("id") Integer doctorId
    ) {
        return appointmentService.getSchedule(doctorId);
    }

}
