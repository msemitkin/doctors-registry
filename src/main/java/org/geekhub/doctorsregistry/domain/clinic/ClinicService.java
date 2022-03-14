package org.geekhub.doctorsregistry.domain.clinic;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.user.User;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.repository.clinic.ClinicEntity;
import org.geekhub.doctorsregistry.repository.clinic.ClinicRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
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
    public void save(@NotNull CreateClinicCommand createClinicCommand) {
        User user = User.newClinic(createClinicCommand.email(), createClinicCommand.password());
        userService.saveUser(user);

        ClinicEntity clinicEntity = getClinicEntity(createClinicCommand);
        clinicRepository.save(clinicEntity);
    }

    public List<ClinicEntity> findAll(int page) {
        return clinicRepository.findAll(PageRequest.of(page, PAGE_SIZE)).toList();
    }

    public ClinicEntity findById(int id) {
        return clinicRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

    private ClinicEntity getClinicEntity(CreateClinicCommand createClinicCommand) {
        return ClinicEntity.create(
            createClinicCommand.name(), createClinicCommand.address(), createClinicCommand.email());
    }

}
