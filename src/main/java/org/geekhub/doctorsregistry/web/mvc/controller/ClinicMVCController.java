package org.geekhub.doctorsregistry.web.mvc.controller;

import io.swagger.annotations.Api;
import org.geekhub.doctorsregistry.domain.clinic.ClinicService;
import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.web.dto.clinic.ClinicDTO;
import org.geekhub.doctorsregistry.domain.mapper.ClinicMapper;
import org.geekhub.doctorsregistry.web.dto.doctor.DoctorDTO;
import org.geekhub.doctorsregistry.domain.mapper.DoctorMapper;
import org.geekhub.doctorsregistry.web.dto.clinic.CreateClinicUserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@ApiIgnore
public class ClinicMVCController {

    private final ClinicService clinicService;
    private final ClinicMapper clinicMapper;
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    public ClinicMVCController(
        ClinicService clinicService,
        DoctorService doctorService,
        ClinicMapper clinicMapper,
        DoctorMapper doctorMapper
    ) {
        this.clinicService = clinicService;
        this.doctorService = doctorService;
        this.clinicMapper = clinicMapper;
        this.doctorMapper = doctorMapper;
    }

    @GetMapping("/clinics")
    public String clinics(Model model) {
        List<ClinicDTO> clinics = clinicService.findAll().stream()
            .map(clinicMapper::toDTO)
            .collect(Collectors.toList());
        model.addAttribute("clinics", clinics);
        return "clinics";
    }

    @GetMapping("/clinic")
    public String clinic(@RequestParam("id") Integer clinicId, Model model) {
        ClinicDTO clinic = clinicMapper.toDTO(clinicService.findById(clinicId));
        List<DoctorDTO> doctors = doctorService.findDoctorsByClinic(clinicId).stream()
            .map(doctorMapper::toDTO)
            .collect(Collectors.toList());
        model.addAttribute("clinic", clinic);
        model.addAttribute("doctors", doctors);
        return "clinic";
    }

    @PostMapping("/clinics")
    public String registerClinic(@ModelAttribute("clinic") CreateClinicUserDTO clinicDTO) {
        clinicService.save(clinicDTO);
        return "redirect:/index";
    }
}
