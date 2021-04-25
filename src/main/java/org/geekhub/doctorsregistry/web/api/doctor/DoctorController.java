package org.geekhub.doctorsregistry.web.api.doctor;

import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.domain.mapper.DoctorMapper;
import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.geekhub.doctorsregistry.web.dto.appointment.AppointmentDTO;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.geekhub.doctorsregistry.web.dto.doctor.DoctorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class DoctorController {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final AppointmentMapper appointmentMapper;

    public DoctorController(
        DoctorService doctorService,
        DoctorMapper doctorMapper,
        AppointmentMapper appointmentMapper
    ) {
        this.doctorService = doctorService;
        this.doctorMapper = doctorMapper;
        this.appointmentMapper = appointmentMapper;
    }

    @PostMapping("/api/doctors")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveDoctor(@RequestBody CreateDoctorUserDTO doctorDTO) {
        doctorService.saveDoctor(doctorDTO);
    }

    @GetMapping("/api/doctors")
    public List<DoctorDTO> getAllDoctors() {
        return doctorService.findAll().stream()
            .map(doctorMapper::toDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/api/doctors/{id}")
    public DoctorDTO getDoctorById(@PathVariable("id") Integer id) {
        DoctorEntity found = doctorService.findById(id);
        return doctorMapper.toDTO(found);
    }

    @GetMapping("/api/clinics/{id}/doctors")
    public List<DoctorDTO> getDoctorsByClinicId(@PathVariable("id") Integer id) {
        return doctorService.findDoctorsByClinic(id).stream()
            .map(doctorMapper::toDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/api/doctors/{id}/schedule")
    public Map<LocalDate, List<LocalTime>> getSchedule(
        @PathVariable("id") Integer doctorId
    ) {
        return doctorService.getSchedule(doctorId);
    }

    @GetMapping("/api/doctors/me/appointments/pending")
    public List<AppointmentDTO> getPendingAppointments() {
        return doctorService.getPendingAppointments().stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/api/doctors/me/appointments/archive")
    public List<AppointmentDTO> getArchivedAppointments() {
        return doctorService.getArchivedAppointments().stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList());
    }

}
