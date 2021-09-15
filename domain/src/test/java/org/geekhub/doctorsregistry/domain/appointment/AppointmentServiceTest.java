package org.geekhub.doctorsregistry.domain.appointment;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.domain.patient.PatientService;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentRepository;
import org.geekhub.doctorsregistry.web.security.UsernameExtractor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.Optional;

public class AppointmentServiceTest {

    private static final LocalDateTime TEST_DATE_TIME =
        LocalDateTime.parse("2002-04-17T10:00:00");

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private PatientService patientService;
    @Mock
    private AppointmentValidator appointmentValidator;
    @Mock
    private UsernameExtractor usernameExtractor;
    @Mock
    private AppointmentMapper appointmentMapper;

    private AppointmentService appointmentService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        appointmentService = new AppointmentService(
            appointmentRepository,
            patientService,
            appointmentValidator,
            usernameExtractor,
            appointmentMapper
        );
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void throws_exception_when_appointment_does_not_exist_with_id() {
        Mockito.when(appointmentRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        appointmentService.findById(5);
    }

    @Test
    public void returns_entity_when_exists_with_given_id() {
        AppointmentEntity testAppointment = new AppointmentEntity(1, 2, 3, TEST_DATE_TIME);
        Mockito.when(appointmentRepository.findById(1)).thenReturn(Optional.of(testAppointment));

        AppointmentEntity actual = appointmentService.findById(1);
        Assert.assertEquals(actual, testAppointment);
    }

}
