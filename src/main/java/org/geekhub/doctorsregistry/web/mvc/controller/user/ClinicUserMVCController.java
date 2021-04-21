package org.geekhub.doctorsregistry.web.mvc.controller.user;

import org.geekhub.doctorsregistry.domain.schedule.Schedule;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;

@Controller
@ApiIgnore
public class ClinicUserMVCController {

    private final Schedule schedule;

    public ClinicUserMVCController(Schedule schedule) {
        this.schedule = schedule;
    }

    @GetMapping("/clinics/me/cabinet")
    public String cabinet(Model model) {
        CreateDoctorUserDTO createDoctorUserDTO = new CreateDoctorUserDTO();
        createDoctorUserDTO.setTimetable(new ArrayList<>());
        model.addAttribute("defaultSchedule", schedule.getScheduleMap());
        model.addAttribute("doctor", createDoctorUserDTO);
        return "clinic-cabinet";
    }

}
