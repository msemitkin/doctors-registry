package org.geekhub.doctorsregistry.web.mvc.controller.user;

import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.stream.Collectors;

@Controller
public class DoctorUserMVCController {

    private final DoctorService doctorService;
    private final AppointmentMapper appointmentMapper;

    public DoctorUserMVCController(DoctorService doctorService, AppointmentMapper appointmentMapper) {
        this.doctorService = doctorService;
        this.appointmentMapper = appointmentMapper;
    }

    @GetMapping("/doctors/me/cabinet")
    public String cabinet(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Integer doctorId = doctorService.getIdByEmail(userDetails.getUsername());
        model.addAttribute("archive", doctorService.getArchivedAppointments(doctorId).stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList()));
        model.addAttribute("pending", doctorService.getPendingAppointments(doctorId).stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList()));
        return "doctor-cabinet";
    }

    @PostMapping("/doctors/registration")
    public String registerDoctor(
        @AuthenticationPrincipal UserDetails userDetails,
        @ModelAttribute("doctor") CreateDoctorUserDTO doctor
    ) {
        doctorService.saveDoctor(doctor);
        return "redirect:/clinics/me/cabinet";
    }
}
