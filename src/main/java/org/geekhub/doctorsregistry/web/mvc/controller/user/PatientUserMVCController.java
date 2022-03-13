package org.geekhub.doctorsregistry.web.mvc.controller.user;

import org.geekhub.doctorsregistry.domain.appointment.AppointmentService;
import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.domain.patient.PatientService;
import org.geekhub.doctorsregistry.web.dto.patient.CreatePatientUserDTO;
import org.geekhub.doctorsregistry.web.security.AuthenticationPrincipalExtractor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Controller
@ApiIgnore
public class PatientUserMVCController {

    private final PatientService patientService;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentService appointmentService;
    private final AuthenticationPrincipalExtractor authenticationPrincipalExtractor;

    public PatientUserMVCController(
        PatientService patientService,
        AppointmentMapper appointmentMapper,
        AppointmentService appointmentService,
        AuthenticationPrincipalExtractor authenticationPrincipalExtractor
    ) {
        this.patientService = patientService;
        this.appointmentMapper = appointmentMapper;
        this.appointmentService = appointmentService;
        this.authenticationPrincipalExtractor = authenticationPrincipalExtractor;
    }

    @GetMapping("/patients/me/cabinet")
    public String cabinet(Model model) {
        int patientId = authenticationPrincipalExtractor.getPrincipal().userId();
        model.addAttribute("archive", patientService.getArchivedAppointments(patientId).stream()
            .map(appointmentMapper::toDTO)
            .toList());
        model.addAttribute("pending", patientService.getPendingAppointments(patientId).stream()
            .map(appointmentMapper::toDTO)
            .toList());
        return "patient-cabinet";
    }

    @PostMapping("/patients/me/appointments/delete")
    public String deleteAppointment(@RequestParam("id") Integer appointmentId) {
        int currentPatientId = authenticationPrincipalExtractor.getPrincipal().userId();
        appointmentService.deleteById(currentPatientId, appointmentId);
        return "redirect:/patients/me/cabinet";
    }

    @GetMapping("/patients/registration")
    public String getRegistrationForm(Model model) {
        model.addAttribute("patient", new CreatePatientUserDTO());
        return "patient-registration";
    }

    @PostMapping("/patients/registration")
    public String registerPatient(
        @ModelAttribute("patient") @Valid CreatePatientUserDTO patient,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "patient-registration";
        }
        patientService.save(patient);
        return "redirect:/index";
    }
}
