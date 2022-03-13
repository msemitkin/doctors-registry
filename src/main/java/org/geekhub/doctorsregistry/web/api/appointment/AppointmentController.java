package org.geekhub.doctorsregistry.web.api.appointment;

import org.geekhub.doctorsregistry.domain.appointment.AppointmentService;
import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.web.dto.appointment.CreateAppointmentDTO;
import org.geekhub.doctorsregistry.web.security.AuthenticationPrincipalExtractor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final AuthenticationPrincipalExtractor authenticationPrincipalExtractor;
    private final AppointmentMapper appointmentMapper;

    public AppointmentController(
        AppointmentService appointmentService,
        AuthenticationPrincipalExtractor authenticationPrincipalExtractor,
        AppointmentMapper appointmentMapper
    ) {
        this.appointmentService = appointmentService;
        this.authenticationPrincipalExtractor = authenticationPrincipalExtractor;
        this.appointmentMapper = appointmentMapper;
    }

    @PostMapping("/api/appointments")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAppointment(@Valid CreateAppointmentDTO createAppointmentDTO) {
        int patientId = authenticationPrincipalExtractor.getPrincipal().userId();
        AppointmentEntity appointment = appointmentMapper.toEntity(patientId, createAppointmentDTO);
        appointmentService.create(appointment);
    }

    @DeleteMapping("/api/appointments/{appointment-id}")
    public void deleteById(@PathVariable("appointment-id") Integer appointmentId) {
        int patientId = authenticationPrincipalExtractor.getPrincipal().userId();
        appointmentService.deleteById(patientId, appointmentId);
    }
}
