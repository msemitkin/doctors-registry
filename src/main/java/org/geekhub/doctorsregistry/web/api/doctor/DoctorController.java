package org.geekhub.doctorsregistry.web.api.doctor;

import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.geekhub.doctorsregistry.repository.specialization.SpecializationEntity;
import org.geekhub.doctorsregistry.web.api.appointment.AppointmentDTO;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("api/doctors")
    public DoctorDTO saveDoctor(@RequestBody CreateDoctorUserDTO doctorDTO) {
        SpecializationEntity specialization
            = new SpecializationEntity(doctorDTO.getSpecializationId(), null);
        DoctorEntity doctor
            = new DoctorEntity(null, doctorDTO.getFirstName(), doctorDTO.getLastName(), specialization, doctorDTO.getClinicId(), doctorDTO.getPrice());
        DoctorEntity saved = doctorService.save(doctor);
        return doctorMapper.toDTO(saved);
    }

    @GetMapping("api/doctors")
    public List<DoctorDTO> getAllDoctors() {
        return doctorService.findAll().stream()
            .map(doctorMapper::toDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("api/doctors/{id}")
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

    @GetMapping("/api/doctors/{id}/appointments/pending")
    public List<AppointmentDTO> getPendingAppointments(@PathVariable("id") Integer doctorId) {
        return doctorService.getPendingAppointments(doctorId).stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/api/doctors/{id}/appointments/archive")
    public List<AppointmentDTO> getArchivedAppointments(@PathVariable("id") Integer doctorId) {
        return doctorService.getArchivedAppointments(doctorId).stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList());
    }

    @DeleteMapping("api/doctors/{id}")
    public void deleteDoctorById(@PathVariable("id") Integer id) {
        doctorService.deleteById(id);
    }

}
