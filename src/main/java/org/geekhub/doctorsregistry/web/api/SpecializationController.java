package org.geekhub.doctorsregistry.web.api;

import org.geekhub.doctorsregistry.domain.specialization.SpecializationService;
import org.geekhub.doctorsregistry.web.specialization.SpecializationDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SpecializationController {

    private final SpecializationService specializationService;

    public SpecializationController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @GetMapping("/api/specializations")
    public List<SpecializationDTO> getSpecializations() {
        return specializationService.findAll().stream()
            .map(SpecializationDTO::of)
            .collect(Collectors.toList());
    }
}
