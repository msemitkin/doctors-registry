package org.geekhub.doctorsregistry.mvc.mvc.controller.user;

import org.geekhub.doctorsregistry.domain.AuthenticationResolver;
import org.geekhub.doctorsregistry.domain.doctor.CreateDoctorCommand;
import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.domain.schedule.Schedule;
import org.geekhub.doctorsregistry.domain.specialization.SpecializationService;
import org.geekhub.doctorsregistry.mvc.dto.doctor.CreateDoctorUserDTO;
import org.geekhub.doctorsregistry.mvc.dto.specialization.SpecializationDTO;
import org.geekhub.doctorsregistry.mvc.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.mvc.mapper.SpecializationMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DoctorUserMVCController {

    private final DoctorService doctorService;
    private final AppointmentMapper appointmentMapper;
    private final Schedule schedule;
    private final SpecializationService specializationService;
    private final SpecializationMapper specializationMapper;
    private final AuthenticationResolver authenticationResolver;

    public DoctorUserMVCController(
        DoctorService doctorService,
        AppointmentMapper appointmentMapper,
        Schedule schedule,
        SpecializationService specializationService,
        SpecializationMapper specializationMapper,
        AuthenticationResolver authenticationResolver
    ) {
        this.doctorService = doctorService;
        this.appointmentMapper = appointmentMapper;
        this.schedule = schedule;
        this.specializationService = specializationService;
        this.specializationMapper = specializationMapper;
        this.authenticationResolver = authenticationResolver;
    }

    @GetMapping("/doctors/me/cabinet")
    public String cabinet(Model model) {
        int doctorId = authenticationResolver.getUserId();
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
        @Valid @ModelAttribute("doctor") CreateDoctorUserDTO doctor,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("defaultSchedule", schedule.getScheduleMap());
            List<SpecializationDTO> specializations = specializationService.findAll().stream()
                .map(specializationMapper::toDTO)
                .collect(Collectors.toList());
            model.addAttribute("specializations", specializations);
            return "clinic-cabinet";
        }

        int clinicId = authenticationResolver.getUserId();
        CreateDoctorCommand createDoctorCommand = toCreateDoctorCommand(doctor);
        doctorService.saveDoctor(createDoctorCommand, clinicId);

        return "redirect:/clinics/me/cabinet";
    }

    private CreateDoctorCommand toCreateDoctorCommand(CreateDoctorUserDTO doctor) {
        return new CreateDoctorCommand(
            doctor.getFirstName(),
            doctor.getLastName(),
            doctor.getEmail(),
            doctor.getSpecializationId(),
            doctor.getPrice(),
            doctor.getTimetable(),
            doctor.getPassword(),
            doctor.getPasswordConfirmation()
        );
    }
}
