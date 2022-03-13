package org.geekhub.doctorsregistry.web.api.appointment;

import org.geekhub.doctorsregistry.domain.appointment.AppointmentService;
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

    public AppointmentController(
        AppointmentService appointmentService,
        AuthenticationPrincipalExtractor authenticationPrincipalExtractor
    ) {
        this.appointmentService = appointmentService;
        this.authenticationPrincipalExtractor = authenticationPrincipalExtractor;
    }

    @PostMapping("/api/appointments")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAppointment(@Valid CreateAppointmentDTO createAppointmentDTO) {
        int patientId = authenticationPrincipalExtractor.getPrincipal().userId();
        appointmentService.create(patientId, createAppointmentDTO);
    }

    @DeleteMapping("/api/appointments/{appointment-id}")
    public void deleteById(@PathVariable("appointment-id") Integer appointmentId) {
        int patientId = authenticationPrincipalExtractor.getPrincipal().userId();
        appointmentService.deleteById(patientId, appointmentId);
    }
}
