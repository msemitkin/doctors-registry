package org.geekhub.doctorsregistry.web.mvc.controller.user;

import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.domain.patient.PatientService;
import org.geekhub.doctorsregistry.web.api.appointment.AppointmentMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.Collectors;

@Controller
public class PatientUserMVCController {

    private final PatientService patientService;
    private final AppointmentMapper appointmentMapper;

    public PatientUserMVCController(PatientService patientService, AppointmentMapper appointmentMapper) {
        this.patientService = patientService;
        this.appointmentMapper = appointmentMapper;
    }

    @GetMapping("/patients/me/cabinet")
    public String cabinet(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Integer doctorId = patientService.getIdByEmail(userDetails.getUsername());
        model.addAttribute("archive", patientService.getArchivedAppointments(doctorId).stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList()));
        model.addAttribute("pending", patientService.getPendingAppointments(doctorId).stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList()));
        return "patient-cabinet";
    }
}
