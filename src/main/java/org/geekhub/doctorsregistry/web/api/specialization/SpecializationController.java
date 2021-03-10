package org.geekhub.doctorsregistry.web.api.specialization;

import org.geekhub.doctorsregistry.domain.specialization.SpecializationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SpecializationController {

    private final SpecializationService specializationService;
    private final SpecializationMapper specializationMapper;

    public SpecializationController(
        SpecializationService specializationService,
        SpecializationMapper specializationMapper
    ) {
        this.specializationService = specializationService;
        this.specializationMapper = specializationMapper;
    }

    @GetMapping("/api/specializations")
    public List<SpecializationDTO> getSpecializations() {
        return specializationService.findAll().stream()
            .map(specializationMapper::toDTO)
            .collect(Collectors.toList());
    }
}
