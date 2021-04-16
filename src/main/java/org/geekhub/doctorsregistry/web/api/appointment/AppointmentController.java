package org.geekhub.doctorsregistry.web.api.appointment;

import org.geekhub.doctorsregistry.domain.appointment.AppointmentService;
import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.web.dto.appointment.AppointmentDTO;
import org.geekhub.doctorsregistry.web.dto.appointment.CreateAppointmentDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public void createAppointment(CreateAppointmentDTO appointmentDTO) {
        AppointmentEntity entity = appointmentMapper.toEntity(appointmentDTO);
        appointmentService.create(entity);
    }

    @GetMapping("/api/appointments/{appointment-id}")
    public AppointmentDTO getAppointmentById(
        @PathVariable("appointment-id") Integer appointmentId
    ) {
        AppointmentEntity entity = appointmentService.findById(appointmentId);
        return appointmentMapper.toDTO(entity);
    }

    @DeleteMapping("/api/appointments/{appointment-id}")
    public void deleteById(@PathVariable("appointment-id") Integer id) {
        appointmentService.deleteById(id);
    }
}
