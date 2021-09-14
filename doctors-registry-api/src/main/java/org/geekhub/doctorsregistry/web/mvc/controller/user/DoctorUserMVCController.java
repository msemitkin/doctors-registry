package org.geekhub.doctorsregistry.web.mvc.controller.user;

import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.domain.mapper.SpecializationMapper;
import org.geekhub.doctorsregistry.domain.schedule.Schedule;
import org.geekhub.doctorsregistry.domain.specialization.SpecializationService;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.geekhub.doctorsregistry.web.dto.specialization.SpecializationDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@ApiIgnore
public class DoctorUserMVCController {

    private final DoctorService doctorService;
    private final AppointmentMapper appointmentMapper;
    private final Schedule schedule;
    private final SpecializationService specializationService;
    private final SpecializationMapper specializationMapper;

    public DoctorUserMVCController(
        DoctorService doctorService,
        AppointmentMapper appointmentMapper,
        Schedule schedule,
        SpecializationService specializationService,
        SpecializationMapper specializationMapper
    ) {
        this.doctorService = doctorService;
        this.appointmentMapper = appointmentMapper;
        this.schedule = schedule;
        this.specializationService = specializationService;
        this.specializationMapper = specializationMapper;
    }

    @GetMapping("/doctors/me/cabinet")
    public String cabinet(Model model) {
        model.addAttribute("archive", doctorService.getArchivedAppointments().stream()
            .map(appointmentMapper::toDTO)
            .collect(Collectors.toList()));
        model.addAttribute("pending", doctorService.getPendingAppointments().stream()
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
        doctorService.saveDoctor(doctor);
        return "redirect:/clinics/me/cabinet";
    }
}
