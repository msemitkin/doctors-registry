package org.geekhub.doctorsregistry.domain.clinic;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.mapper.ClinicMapper;
import org.geekhub.doctorsregistry.domain.user.User;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.repository.clinic.ClinicEntity;
import org.geekhub.doctorsregistry.repository.clinic.ClinicRepository;
import org.geekhub.doctorsregistry.web.dto.clinic.CreateClinicUserDTO;
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
    private final ClinicMapper clinicMapper;

    public ClinicService(
        ClinicRepository clinicRepository,
        UserService userService,
        ClinicMapper clinicMapper
    ) {
        this.clinicRepository = clinicRepository;
        this.userService = userService;
        this.clinicMapper = clinicMapper;
    }

    @Transactional
    public void save(@NotNull CreateClinicUserDTO clinicDTO) {
        User user = User.newClinic(clinicDTO.getEmail(), clinicDTO.getPassword());
        userService.saveUser(user);

        ClinicEntity clinicEntity = clinicMapper.toEntity(clinicDTO);
        clinicRepository.save(clinicEntity);
    }

    public List<ClinicEntity> findAll(int page) {
        return clinicRepository.findAll(PageRequest.of(page, PAGE_SIZE)).toList();
    }

    public ClinicEntity findById(int id) {
        return clinicRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

}
