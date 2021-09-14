package org.geekhub.doctorsregistry.web.api.appointment;

import org.geekhub.doctorsregistry.domain.appointment.AppointmentService;
import org.geekhub.doctorsregistry.web.dto.appointment.CreateAppointmentDTO;
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

    public AppointmentController(
        AppointmentService appointmentService
    ) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/api/appointments")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAppointment(@Valid CreateAppointmentDTO createAppointmentDTO) {
        appointmentService.create(createAppointmentDTO);
    }

    @DeleteMapping("/api/appointments/{appointment-id}")
    public void deleteById(@PathVariable("appointment-id") Integer id) {
        appointmentService.deleteById(id);
    }
}
