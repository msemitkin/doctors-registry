package org.geekhub.doctorsregistry.web.mvc.controller;

import org.geekhub.doctorsregistry.domain.appointment.AppointmentService;
import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.web.dto.doctor.DoctorDTO;
import org.geekhub.doctorsregistry.domain.mapper.DoctorMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    public DoctorMVCController(
        DoctorService doctorService,
        DoctorMapper doctorMapper,
        AppointmentService appointmentService
    ) {
        this.doctorService = doctorService;
        this.doctorMapper = doctorMapper;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/doctors")
    public String doctors(Model model) {
        List<DoctorDTO> doctors = doctorService.findAll().stream()
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
        return "doctor";
    }

    @PostMapping("doctor/appointments")
    public String makeAppointment(
        @AuthenticationPrincipal User user,
        @RequestParam("datetime") String inputDateTime,
        @RequestParam("doctor-id") Integer doctorId
    ) {
        LocalDateTime dateTime = LocalDateTime.parse(inputDateTime);
        appointmentService.create(user, doctorId, dateTime);
        return "redirect:/doctor?id=" + doctorId;
    }
}
