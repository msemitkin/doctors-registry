package org.geekhub.doctorsregistry.api.clinic;

import org.geekhub.doctorsregistry.api.dto.clinic.ClinicDTO;
import org.geekhub.doctorsregistry.api.dto.clinic.CreateClinicUserDTO;
import org.geekhub.doctorsregistry.api.mapper.ClinicMapper;
import org.geekhub.doctorsregistry.domain.clinic.ClinicService;
import org.geekhub.doctorsregistry.domain.clinic.CreateClinicCommand;
import org.geekhub.doctorsregistry.repository.clinic.ClinicEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ClinicController {

    private final ClinicService clinicService;
    private final ClinicMapper clinicMapper;

    public ClinicController(
        ClinicService clinicService,
        ClinicMapper clinicMapper
    ) {
        this.clinicService = clinicService;
        this.clinicMapper = clinicMapper;
    }

    @PostMapping("/api/clinics")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveClinic(@Valid CreateClinicUserDTO clinicDTO) {
        //TODO ensure admin rights
        CreateClinicCommand createClinicCommand = toCreateClinicCommand(clinicDTO);
        clinicService.save(createClinicCommand);
    }

    @GetMapping("/api/clinics/pages/{page}")
    public List<ClinicDTO> findAllClinics(@PathVariable(value = "page") int page) {
        //TODO rework pages
        if (page < 0) {
            page = 0;
        }
        return clinicService.findAll(page).stream()
            .map(clinicMapper::toDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/api/clinics/{id}")
    public ClinicDTO findClinicById(@PathVariable("id") int id) {
        ClinicEntity found = clinicService.findById(id);
        return clinicMapper.toDTO(found);
    }

    private CreateClinicCommand toCreateClinicCommand(CreateClinicUserDTO clinicDTO) {
        return new CreateClinicCommand(
            clinicDTO.getName(),
            clinicDTO.getAddress(),
            clinicDTO.getEmail(),
            clinicDTO.getPassword(),
            clinicDTO.getPasswordConfirmation()
        );
    }

}
