package org.geekhub.doctorsregistry.domain.clinic;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.mapper.ClinicMapper;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.repository.clinic.ClinicEntity;
import org.geekhub.doctorsregistry.repository.clinic.ClinicRepository;
import org.geekhub.doctorsregistry.web.dto.clinic.CreateClinicUserDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ClinicService {

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
    public void save(CreateClinicUserDTO clinicDTO) {
        ClinicEntity clinicEntity = clinicMapper.toEntity(clinicDTO);
        userService.saveUser(clinicDTO);
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
