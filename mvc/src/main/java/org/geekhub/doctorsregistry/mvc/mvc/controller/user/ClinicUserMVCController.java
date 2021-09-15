package org.geekhub.doctorsregistry.mvc.mvc.controller.user;

import org.geekhub.doctorsregistry.domain.mapper.SpecializationMapper;
import org.geekhub.doctorsregistry.domain.schedule.Schedule;
import org.geekhub.doctorsregistry.domain.specialization.SpecializationService;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.geekhub.doctorsregistry.web.dto.specialization.SpecializationDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ClinicUserMVCController {

    private final Schedule schedule;
    private final SpecializationService specializationService;
    private final SpecializationMapper specializationMapper;

    public ClinicUserMVCController(
        Schedule schedule,
        SpecializationService specializationService,
        SpecializationMapper specializationMapper
    ) {
        this.schedule = schedule;
        this.specializationService = specializationService;
        this.specializationMapper = specializationMapper;
    }

    @GetMapping("/clinics/me/cabinet")
    public String cabinet(Model model) {
        CreateDoctorUserDTO createDoctorUserDTO = new CreateDoctorUserDTO();
        createDoctorUserDTO.setTimetable(new ArrayList<>());
        List<SpecializationDTO> specializations = specializationService.findAll().stream()
            .map(specializationMapper::toDTO)
            .collect(Collectors.toList());
        model.addAttribute("specializations", specializations);
        model.addAttribute("defaultSchedule", schedule.getScheduleMap());
        model.addAttribute("doctor", createDoctorUserDTO);
        return "clinic-cabinet";
    }

}
