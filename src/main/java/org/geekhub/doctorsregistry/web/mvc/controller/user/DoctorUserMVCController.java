package org.geekhub.doctorsregistry.web.mvc.controller.user;

import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.domain.mapper.SpecializationMapper;
import org.geekhub.doctorsregistry.domain.schedule.Schedule;
import org.geekhub.doctorsregistry.domain.specialization.SpecializationService;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.geekhub.doctorsregistry.web.dto.specialization.SpecializationDTO;
import org.geekhub.doctorsregistry.web.security.UsernameExtractor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Controller
@ApiIgnore
public class DoctorUserMVCController {

    private final DoctorService doctorService;
    private final AppointmentMapper appointmentMapper;
    private final Schedule schedule;
    private final SpecializationService specializationService;
    private final SpecializationMapper specializationMapper;
    private final UsernameExtractor usernameExtractor;

    public DoctorUserMVCController(
        DoctorService doctorService,
        AppointmentMapper appointmentMapper,
        Schedule schedule,
        SpecializationService specializationService,
        SpecializationMapper specializationMapper,
        UsernameExtractor usernameExtractor
    ) {
        this.doctorService = doctorService;
        this.appointmentMapper = appointmentMapper;
        this.schedule = schedule;
        this.specializationService = specializationService;
        this.specializationMapper = specializationMapper;
        this.usernameExtractor = usernameExtractor;
    }

    @GetMapping("/doctors/me/cabinet")
    public String cabinet(Model model) {
        Integer currentDoctorId = usernameExtractor.getDoctorId();
        model.addAttribute("archive", doctorService.getArchivedAppointments(currentDoctorId).stream()
            .map(appointmentMapper::toDTO)
            .toList()
        );
        model.addAttribute("pending", doctorService.getPendingAppointments(currentDoctorId).stream()
            .map(appointmentMapper::toDTO)
            .toList()
        );
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
                .toList();
            model.addAttribute("specializations", specializations);
            return "clinic-cabinet";
        }
        doctorService.saveDoctor(doctor);
        return "redirect:/clinics/me/cabinet";
    }
}
