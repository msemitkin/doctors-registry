package org.geekhub.doctorsregistry.domain.specialization;

import org.geekhub.doctorsregistry.repository.specialization.SpecializationEntity;
import org.geekhub.doctorsregistry.repository.specialization.SpecializationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SpecializationService {

    private final SpecializationRepository specializationRepository;

    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    public List<SpecializationEntity> findAll() {
        return StreamSupport.stream(specializationRepository.findAll().spliterator(), false)
            .toList();
    }

}
