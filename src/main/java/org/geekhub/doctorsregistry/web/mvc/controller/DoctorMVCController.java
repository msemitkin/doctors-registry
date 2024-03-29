package org.geekhub.doctorsregistry.web.mvc.controller;

import org.geekhub.doctorsregistry.domain.appointment.AppointmentService;
import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.domain.mapper.DoctorMapper;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.web.dto.appointment.CreateAppointmentDTO;
import org.geekhub.doctorsregistry.web.dto.doctor.DoctorDTO;
import org.geekhub.doctorsregistry.web.security.AuthenticationPrincipalExtractor;
import org.springframework.lang.Nullable;
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

@Controller
@ApiIgnore
public class DoctorMVCController {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final AppointmentService appointmentService;
    private final AuthenticationPrincipalExtractor authenticationPrincipalExtractor;
    private final AppointmentMapper appointmentMapper;

    public DoctorMVCController(
        DoctorService doctorService,
        DoctorMapper doctorMapper,
        AppointmentService appointmentService,
        AuthenticationPrincipalExtractor authenticationPrincipalExtractor,
        AppointmentMapper appointmentMapper
    ) {
        this.doctorService = doctorService;
        this.doctorMapper = doctorMapper;
        this.appointmentService = appointmentService;
        this.authenticationPrincipalExtractor = authenticationPrincipalExtractor;
        this.appointmentMapper = appointmentMapper;
    }

    @GetMapping("/doctors")
    public String doctors(
        @RequestParam(value = "page", required = false) Integer inputPage,
        Model model
    ) {
        int page = adjustPage(inputPage);
        List<DoctorDTO> doctors = doctorService.findAll(page).stream()
            .map(doctorMapper::toDTO)
            .toList();
        model.addAttribute("doctors", doctors);
        return "doctors";
    }

    @GetMapping("/doctor")
    public String doctor(@RequestParam("id") int doctorId, Model model) {
        DoctorDTO doctor = doctorMapper.toDTO(doctorService.findById(doctorId));
        model.addAttribute("doctor", doctor);
        Map<LocalDate, List<LocalTime>> schedule = doctorService.getSchedule(doctorId);
        model.addAttribute("schedule", schedule);
        model.addAttribute("appointment", new CreateAppointmentDTO());
        return "doctor";
    }

    @PostMapping("/doctor/appointments")
    public String makeAppointment(@Valid CreateAppointmentDTO appointmentDTO) {
        int currentPatientId = authenticationPrincipalExtractor.getPrincipal().userId();
        AppointmentEntity appointment = appointmentMapper.toEntity(currentPatientId, appointmentDTO);
        appointmentService.create(appointment);
        return "redirect:/doctor?id=" + appointmentDTO.getDoctorId();
    }

    private int adjustPage(@Nullable Integer givenPage) {
        if (givenPage == null || givenPage < 0) {
            return 0;
        } else {
            return givenPage;
        }
    }
}
