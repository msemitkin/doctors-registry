package org.geekhub.doctorsregistry.domain.specialization;

import org.geekhub.doctorsregistry.repository.specialization.SpecializationEntity;
import org.geekhub.doctorsregistry.repository.specialization.SpecializationRepository;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

public class SpecializationServiceTest {

    @Mock
    private SpecializationRepository specializationRepository;

    private SpecializationService specializationService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        specializationService = new SpecializationService(specializationRepository);
    }

    @Test
    public void returns_empty_list_when_there_are_no_specializations_found() {
        Mockito.when(specializationRepository.findAll()).thenReturn(Collections.emptyList());
        Assert.assertEquals(specializationService.findAll(), Collections.emptyList());
    }

    @Test
    public void returns_specializations_correct() {
        List<SpecializationEntity> specializationEntitiesList = List.of(
            new SpecializationEntity(1, "spec1"),
            new SpecializationEntity(2, "spec2"),
            new SpecializationEntity(3, "spec3")
        );
        Mockito.when(specializationRepository.findAll()).thenReturn(specializationEntitiesList);
        Assert.assertEquals(specializationService.findAll(), specializationEntitiesList);
    }
}