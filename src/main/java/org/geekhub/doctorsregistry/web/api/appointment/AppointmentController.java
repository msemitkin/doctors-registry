package org.geekhub.doctorsregistry.web.api.appointment;

import org.geekhub.doctorsregistry.domain.appointment.AppointmentService;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    public AppointmentController(AppointmentService appointmentService, AppointmentMapper appointmentMapper) {
        this.appointmentService = appointmentService;
        this.appointmentMapper = appointmentMapper;
    }

    @GetMapping("/appointments/{appointment-id}")
    public AppointmentDTO getAppointmentById(@PathVariable("appointment-id") Integer appointmentId) {
        AppointmentEntity entity = appointmentService.findById(appointmentId);
        return appointmentMapper.toDTO(entity);
    }

    @GetMapping("/api/patients/{patient-id}/appointments")
    public List<AppointmentDTO> getPatientAppointments(@PathVariable("patient-id") Integer patientId) {
        return appointmentService.findByPatientId(patientId).stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/api/doctors/{doctor-id}/appointments")
    public List<AppointmentDTO> getDoctorAppointments(@PathVariable("doctor-id") Integer doctorId) {
        return appointmentService.findByDoctorId(doctorId).stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList());
    }

}
