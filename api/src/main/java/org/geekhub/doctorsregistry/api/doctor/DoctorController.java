package org.geekhub.doctorsregistry.api.doctor;

import org.geekhub.doctorsregistry.api.dto.appointment.AppointmentDTO;
import org.geekhub.doctorsregistry.api.dto.doctor.CreateDoctorUserDTO;
import org.geekhub.doctorsregistry.api.dto.doctor.DoctorDTO;
import org.geekhub.doctorsregistry.api.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.api.mapper.DoctorMapper;
import org.geekhub.doctorsregistry.domain.AuthenticationResolver;
import org.geekhub.doctorsregistry.domain.doctor.CreateDoctorCommand;
import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
    private final AuthenticationResolver authenticationResolver;

    public DoctorController(
        DoctorService doctorService,
        DoctorMapper doctorMapper,
        AppointmentMapper appointmentMapper,
        AuthenticationResolver authenticationResolver
    ) {
        this.doctorService = doctorService;
        this.doctorMapper = doctorMapper;
        this.appointmentMapper = appointmentMapper;
        this.authenticationResolver = authenticationResolver;
    }

    @PostMapping("/api/doctors")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveDoctor(@Valid @RequestBody CreateDoctorUserDTO doctorDTO) {
        int clinicId = authenticationResolver.getUserId();
        CreateDoctorCommand createDoctorCommand = toCreateDoctorCommand(doctorDTO);
        doctorService.saveDoctor(createDoctorCommand, clinicId);
    }

    @GetMapping("/api/doctors/pages/{page}")
    public List<DoctorDTO> getAllDoctors(@PathVariable(value = "page") int page) {
        if (page < 0) {
            page = 0;
        }
        return doctorService.findAll(page).stream()
            .map(doctorMapper::toDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/api/doctors/{id}")
    public DoctorDTO getDoctorById(@PathVariable("id") int id) {
        DoctorEntity found = doctorService.findById(id);
        return doctorMapper.toDTO(found);
    }

    @GetMapping("/api/clinics/{id}/doctors")
    public List<DoctorDTO> getDoctorsByClinicId(@PathVariable("id") int id) {
        return doctorService.findDoctorsByClinic(id).stream()
            .map(doctorMapper::toDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/api/doctors/{id}/schedule")
    public Map<LocalDate, List<LocalTime>> getSchedule(
        @PathVariable("id") int doctorId
    ) {
        return doctorService.getSchedule(doctorId);
    }

    @GetMapping("/api/doctors/me/appointments/pending")
    public List<AppointmentDTO> getPendingAppointments() {
        int doctorId = authenticationResolver.getUserId();
        return doctorService.getPendingAppointments(doctorId).stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/api/doctors/me/appointments/archive")
    public List<AppointmentDTO> getArchivedAppointments() {
        int doctorId = authenticationResolver.getUserId();
        return doctorService.getArchivedAppointments(doctorId).stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList());
    }

    private CreateDoctorCommand toCreateDoctorCommand(CreateDoctorUserDTO doctorDTO) {
        return new CreateDoctorCommand(
            doctorDTO.getFirstName(),
            doctorDTO.getLastName(),
            doctorDTO.getEmail(),
            doctorDTO.getSpecializationId(),
            doctorDTO.getPrice(),
            doctorDTO.getTimetable(),
            doctorDTO.getPassword(),
            doctorDTO.getPasswordConfirmation()
        );
    }

}
