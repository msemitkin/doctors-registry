package org.geekhub.doctorsregistry.web.mvc.controller.user;

import org.geekhub.doctorsregistry.domain.doctor.CreateDoctorCommand;
import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.domain.mapper.SpecializationMapper;
import org.geekhub.doctorsregistry.domain.schedule.DayTimeSpliterator;
import org.geekhub.doctorsregistry.domain.schedule.Schedule;
import org.geekhub.doctorsregistry.domain.specialization.SpecializationService;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.geekhub.doctorsregistry.web.dto.specialization.SpecializationDTO;
import org.geekhub.doctorsregistry.web.security.AuthenticationPrincipalExtractor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

@Controller
@ApiIgnore
public class DoctorUserMVCController {

    private final DoctorService doctorService;
    private final AppointmentMapper appointmentMapper;
    private final Schedule schedule;
    private final SpecializationService specializationService;
    private final SpecializationMapper specializationMapper;
    private final AuthenticationPrincipalExtractor authenticationPrincipalExtractor;
    private final DayTimeSpliterator dayTimeSpliterator;

    public DoctorUserMVCController(
        DoctorService doctorService,
        AppointmentMapper appointmentMapper,
        Schedule schedule,
        SpecializationService specializationService,
        SpecializationMapper specializationMapper,
        AuthenticationPrincipalExtractor authenticationPrincipalExtractor,
        DayTimeSpliterator dayTimeSpliterator
    ) {
        this.doctorService = doctorService;
        this.appointmentMapper = appointmentMapper;
        this.schedule = schedule;
        this.specializationService = specializationService;
        this.specializationMapper = specializationMapper;
        this.authenticationPrincipalExtractor = authenticationPrincipalExtractor;
        this.dayTimeSpliterator = dayTimeSpliterator;
    }

    @GetMapping("/doctors/me/cabinet")
    public String cabinet(Model model) {
        int currentDoctorId = authenticationPrincipalExtractor.getPrincipal().userId();
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
        int currentClinicId = authenticationPrincipalExtractor.getPrincipal().userId();
        CreateDoctorCommand createDoctorCommand = getCreateDoctorCommand(doctor, currentClinicId);
        doctorService.saveDoctor(createDoctorCommand);
        return "redirect:/clinics/me/cabinet";
    }

    private CreateDoctorCommand getCreateDoctorCommand(CreateDoctorUserDTO doctorDTO, int currentClinicId) {
        return new CreateDoctorCommand(
            doctorDTO.getFirstName(),
            doctorDTO.getLastName(),
            doctorDTO.getEmail(),
            doctorDTO.getPassword(),
            doctorDTO.getSpecializationId(),
            currentClinicId,
            doctorDTO.getPrice(),
            new HashSet<>(dayTimeSpliterator.splitToDayTime(doctorDTO.getTimetable()))
        );
    }
}
