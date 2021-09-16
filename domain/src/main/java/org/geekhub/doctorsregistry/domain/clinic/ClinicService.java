package org.geekhub.doctorsregistry.domain.clinic;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.role.Role;
import org.geekhub.doctorsregistry.domain.user.User;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.repository.clinic.ClinicEntity;
import org.geekhub.doctorsregistry.repository.clinic.ClinicRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClinicService {
    private static final int PAGE_SIZE = 10;

    private final ClinicRepository clinicRepository;
    private final UserService userService;

    public ClinicService(
        ClinicRepository clinicRepository,
        UserService userService
    ) {
        this.clinicRepository = clinicRepository;
        this.userService = userService;
    }

    @Transactional
    public void save(CreateClinicCommand createClinicCommand) {
        User user = toUser(createClinicCommand);
        userService.saveUser(user);

        ClinicEntity clinicEntity = toClinicEntity(createClinicCommand);
        clinicRepository.save(clinicEntity);
    }

    public List<ClinicEntity> findAll(int page) {
        return clinicRepository.findAll(PageRequest.of(page, PAGE_SIZE)).toList();
    }

    public ClinicEntity findById(int id) {
        return clinicRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

    public Integer getIdByEmail(String email) {
        return clinicRepository.getIdByEmail(email);
    }

    private User toUser(CreateClinicCommand createClinicCommand) {
        return new User(
            createClinicCommand.getEmail(),
            createClinicCommand.getPassword(),
            createClinicCommand.getPasswordConfirmation(),
            Role.CLINIC
        );
    }

    private ClinicEntity toClinicEntity(CreateClinicCommand createClinicCommand) {
        return ClinicEntity.of(
            createClinicCommand.getName(),
            createClinicCommand.getAddress(),
            createClinicCommand.getEmail()
        );
    }

}
