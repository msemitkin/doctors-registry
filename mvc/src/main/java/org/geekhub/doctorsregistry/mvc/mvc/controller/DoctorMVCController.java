package org.geekhub.doctorsregistry.mvc.mvc.controller;

import org.geekhub.doctorsregistry.domain.AuthenticationResolver;
import org.geekhub.doctorsregistry.domain.appointment.AppointmentService;
import org.geekhub.doctorsregistry.domain.appointment.CreateAppointmentCommand;
import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.mvc.dto.appointment.CreateAppointmentDTO;
import org.geekhub.doctorsregistry.mvc.dto.doctor.DoctorDTO;
import org.geekhub.doctorsregistry.mvc.mapper.DoctorMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DoctorMVCController {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final AppointmentService appointmentService;
    private final AuthenticationResolver authenticationResolver;

    public DoctorMVCController(
        DoctorService doctorService,
        DoctorMapper doctorMapper,
        AppointmentService appointmentService,
        AuthenticationResolver authenticationResolver
    ) {
        this.doctorService = doctorService;
        this.doctorMapper = doctorMapper;
        this.appointmentService = appointmentService;
        this.authenticationResolver = authenticationResolver;
    }

    @GetMapping("/doctors")
    public String doctors(
        @RequestParam(value = "page", required = false) Integer page,
        Model model
    ) {
        if (page == null || page < 0) {
            page = 0;
        }
        List<DoctorDTO> doctors = doctorService.findAll(page).stream()
            .map(doctorMapper::toDTO)
            .collect(Collectors.toList());
        model.addAttribute("doctors", doctors);
        return "doctors";
    }

    @GetMapping("/doctor")
    public String doctor(@RequestParam("id") Integer doctorId, Model model) {
        DoctorDTO doctor = doctorMapper.toDTO(doctorService.findById(doctorId));
        model.addAttribute("doctor", doctor);
        Map<LocalDate, List<LocalTime>> schedule = doctorService.getSchedule(doctorId);
        model.addAttribute("schedule", schedule);
        model.addAttribute("appointment", new CreateAppointmentDTO());
        return "doctor";
    }

    @PostMapping("/doctor/appointments")
    public String makeAppointment(@Valid CreateAppointmentDTO appointmentDTO) {
        int patientId = authenticationResolver.getUserId();
        CreateAppointmentCommand createAppointmentCommand = new CreateAppointmentCommand(
            patientId,
            appointmentDTO.getDoctorId(),
            LocalDateTime.parse(appointmentDTO.getInputDateTime())
        );
        appointmentService.create(createAppointmentCommand);
        return "redirect:/doctor?id=" + appointmentDTO.getDoctorId();
    }
}
