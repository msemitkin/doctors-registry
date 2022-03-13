package org.geekhub.doctorsregistry.web.api.appointment;

import org.geekhub.doctorsregistry.domain.appointment.AppointmentService;
import org.geekhub.doctorsregistry.web.dto.appointment.CreateAppointmentDTO;
import org.geekhub.doctorsregistry.web.security.UsernameExtractor;
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
    private final UsernameExtractor usernameExtractor;

    public AppointmentController(
        AppointmentService appointmentService,
        UsernameExtractor usernameExtractor
    ) {
        this.appointmentService = appointmentService;
        this.usernameExtractor = usernameExtractor;
    }

    @PostMapping("/api/appointments")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAppointment(@Valid CreateAppointmentDTO createAppointmentDTO) {
        Integer patientId = usernameExtractor.getPatientId();
        appointmentService.create(patientId, createAppointmentDTO);
    }

    @DeleteMapping("/api/appointments/{appointment-id}")
    public void deleteById(@PathVariable("appointment-id") Integer appointmentId) {
        Integer patientId = usernameExtractor.getPatientId();
        appointmentService.deleteById(patientId, appointmentId);
    }
}
