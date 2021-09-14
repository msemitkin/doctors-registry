package org.geekhub.doctorsregistry.web.mvc.controller;

import org.geekhub.doctorsregistry.domain.appointment.AppointmentService;
import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.domain.mapper.DoctorMapper;
import org.geekhub.doctorsregistry.web.dto.appointment.CreateAppointmentDTO;
import org.geekhub.doctorsregistry.web.dto.doctor.DoctorDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@ApiIgnore
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
        appointmentService.create(appointmentDTO);
        return "redirect:/doctor?id=" + appointmentDTO.getDoctorId();
    }
}
