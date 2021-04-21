package org.geekhub.doctorsregistry.web.mvc.controller.user;

import org.geekhub.doctorsregistry.domain.appointment.AppointmentService;
import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.domain.patient.PatientService;
import org.geekhub.doctorsregistry.web.dto.patient.CreatePatientUserDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import java.util.stream.Collectors;

@Controller
@ApiIgnore
public class PatientUserMVCController {

    private final PatientService patientService;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentService appointmentService;

    public PatientUserMVCController(
        PatientService patientService,
        AppointmentMapper appointmentMapper,
        AppointmentService appointmentService
    ) {
        this.patientService = patientService;
        this.appointmentMapper = appointmentMapper;
        this.appointmentService = appointmentService;
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

    @PostMapping("/patients/me/appointments/delete")
    public String deleteAppointment(@RequestParam("id") Integer appointmentId) {
        appointmentService.deleteById(appointmentId);
        return "redirect:/patients/me/cabinet";
    }

    @GetMapping("/patients/registration")
    public String getRegistrationForm(Model model) {
        model.addAttribute("patient", new CreatePatientUserDTO());
        return "patient-registration";
    }

    @PostMapping("/patients/registration")
    public String registerPatient(@ModelAttribute("patient") CreatePatientUserDTO patient) {
        patientService.save(patient);
        return "redirect:/index";
    }
}
