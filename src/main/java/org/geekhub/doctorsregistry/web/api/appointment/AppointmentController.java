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

    public AppointmentController(
        AppointmentService appointmentService,
        AppointmentMapper appointmentMapper
    ) {
        this.appointmentService = appointmentService;
        this.appointmentMapper = appointmentMapper;
    }

    @GetMapping("/appointments/{appointment-id}")
    public AppointmentDTO getAppointmentById(
        @PathVariable("appointment-id") Integer appointmentId
    ) {
        AppointmentEntity entity = appointmentService.findById(appointmentId);
        return appointmentMapper.toDTO(entity);
    }

    @GetMapping("/api/patients/{patient-id}/appointments/pending")
    public List<AppointmentDTO> getPatientPendingAppointments(
        @PathVariable("patient-id") Integer patientId
    ) {
        return appointmentService.findPendingAppointmentsByPatientId(patientId).stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/api/doctors/{doctor-id}/appointments/pending")
    public List<AppointmentDTO> getDoctorPendingAppointments(
        @PathVariable("doctor-id") Integer doctorId
    ) {
        return appointmentService.findPendingAppointmentsByDoctorId(doctorId).stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/api/patients/{patient-id}/appointments/archive")
    public List<AppointmentDTO> getPatientArchivedAppointments(
        @PathVariable("patient-id") Integer patientId
    ) {
        return appointmentService.findArchivedAppointmentsByPatientId(patientId).stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/api/doctors/{doctor-id}/appointments/archive")
    public List<AppointmentDTO> getDoctorArchivedAppointments(
        @PathVariable("doctor-id") Integer doctorId
    ) {
        return appointmentService.findArchivedAppointmentsByDoctorId(doctorId).stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList());
    }

}
