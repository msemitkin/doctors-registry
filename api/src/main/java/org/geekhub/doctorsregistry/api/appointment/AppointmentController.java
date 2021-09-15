package org.geekhub.doctorsregistry.api.appointment;

import org.geekhub.doctorsregistry.api.dto.appointment.CreateAppointmentDTO;
import org.geekhub.doctorsregistry.domain.AuthenticationResolver;
import org.geekhub.doctorsregistry.domain.appointment.AppointmentService;
import org.geekhub.doctorsregistry.domain.appointment.CreateAppointmentCommand;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final AuthenticationResolver authenticationResolver;

    public AppointmentController(
        AppointmentService appointmentService,
        AuthenticationResolver authenticationResolver
    ) {
        this.appointmentService = appointmentService;
        this.authenticationResolver = authenticationResolver;
    }

    @PostMapping("/api/appointments")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAppointment(@Valid CreateAppointmentDTO createAppointmentDTO) {
        int patientId = authenticationResolver.getUserId();
        CreateAppointmentCommand appointmentCommand = new CreateAppointmentCommand(
            patientId,
            createAppointmentDTO.getDoctorId(),
            LocalDateTime.parse(createAppointmentDTO.getInputDateTime())
        );
        appointmentService.create(appointmentCommand);
    }

    @DeleteMapping("/api/appointments/{appointment-id}")
    public void deleteById(@PathVariable("appointment-id") Integer id) {
        //TODO verify patient own this appointment
        appointmentService.deleteById(id);
    }
}
