package org.geekhub.doctorsregistry.domain.clinic;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.user.CreateUserDTO;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.repository.clinic.ClinicEntity;
import org.geekhub.doctorsregistry.repository.clinic.ClinicRepository;
import org.geekhub.doctorsregistry.web.dto.clinic.RegisterClinicDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ClinicService {

    private final ClinicRepository clinicRepository;
    private final UserService userService;

    public ClinicService(ClinicRepository clinicRepository, UserService userService) {
        this.clinicRepository = clinicRepository;
        this.userService = userService;
    }

    @Transactional
    public void save(RegisterClinicDTO clinicDTO) {
        ClinicEntity clinicEntity = new ClinicEntity(
            null,
            clinicDTO.getName(),
            clinicDTO.getAddress()
        );
        CreateUserDTO userDTO = new CreateUserDTO(
            clinicDTO.getEmail(),
            clinicDTO.getPassword(),
            clinicDTO.getPasswordConfirmation(),
            new String[]{"CLINIC"}
        );
        userService.saveUser(userDTO);
        clinicRepository.save(clinicEntity);
    }

    public List<ClinicEntity> findAll() {
        return StreamSupport.stream(clinicRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }

    public ClinicEntity findById(int id) {
        return clinicRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

    public void deleteById(int id) {
        if (clinicRepository.existsById(id)) {
            clinicRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id);
        }
    }

}
