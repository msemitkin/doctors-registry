package org.geekhub.doctorsregistry.domain.appointment;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.datime.ZonedTime;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentRepository;
import org.geekhub.doctorsregistry.repository.patient.PatientServiceRepository;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.Optional;

public class AppointmentServiceTest {

    private static final LocalDateTime TEST_DATE_TIME =
        LocalDateTime.parse("2002-04-17T10:00:00");

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private ZonedTime zonedTime;
    @Mock
    private PatientServiceRepository patientRepository;

    private AppointmentService appointmentService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        appointmentService = new AppointmentService(zonedTime, appointmentRepository, patientRepository);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void throws_exception_when_appointment_is_null() {
        Mockito.doNothing().when(appointmentRepository).create(Mockito.any(AppointmentEntity.class));
        appointmentService.create(null);
    }

    @Test
    public void saves_appointment_with_null_id() {
        AppointmentEntity appointmentEntity = new AppointmentEntity(null, 1, 1, TEST_DATE_TIME);

        Mockito.doNothing().when(appointmentRepository).create(Mockito.any(AppointmentEntity.class));
        Mockito.when(appointmentRepository.doctorAvailable(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(appointmentRepository.patientDoNotHaveAppointment(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(appointmentRepository.patientDoNotHaveAppointment(Mockito.anyInt(), Mockito.any())).thenReturn(true);
        Mockito.when(zonedTime.now()).thenReturn(TEST_DATE_TIME.minusDays(1));

        appointmentService.create(appointmentEntity);
    }

    @DataProvider(name = "throws_exception_when_appointment_has_null_fields_parameters")
    private Object[][] throws_exception_when_appointment_has_null_fields_parameters() {
        return new Object[][]{
            {new AppointmentEntity(null, null, 100, TEST_DATE_TIME)},
            {new AppointmentEntity(null, 100, null, TEST_DATE_TIME)},
            {new AppointmentEntity(null, 1, 1, null)}
        };
    }

    @Test(
        expectedExceptions = IllegalArgumentException.class,
        dataProvider = "throws_exception_when_appointment_has_null_fields_parameters"
    )
    public void throws_exception_when_appointment_has_null_fields(AppointmentEntity appointmentEntity) {
        Mockito.doNothing().when(appointmentRepository).create(Mockito.any(AppointmentEntity.class));
        appointmentService.create(appointmentEntity);
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


    @DataProvider(name = "throws_exception_when_create_appointment_for_past_time_parameters")
    public Object[][] throws_exception_when_create_appointment_for_past_time_parameters() {
        return new Object[][]{
            {"2021-03-17T10:00:00", "2021-03-17T09:59:00"},
            {"2021-03-17T10:00:00", "2021-03-17T10:00:00"},
            {"2021-03-17T10:00:00", "2020-03-17T12:00:00"},
            {"2021-03-17T10:00:00", "2021-02-17T12:00:00"},
            {"2021-03-17T10:00:00", "2021-02-16T12:00:00"},
            {"2021-03-29T06:00:00", "2021-03-29T14:00:00"},
            {"2021-03-29T10:00:00", "2021-03-29T14:00:00"},
            {"2021-03-29T00:00:00", "2021-03-29T14:00:00"}
        };
    }

    @Test(
        expectedExceptions = TimeNotAllowed.class,
        dataProvider = "throws_exception_when_create_appointment_for_past_time_parameters"
    )
    public void throws_exception_when_create_appointment_for_not_allowed_time(String timeNow, String appointmentTime) {
        Mockito.when(zonedTime.now()).thenReturn(LocalDateTime.parse(timeNow));
        Mockito.doNothing().when(appointmentRepository).create(Mockito.any(AppointmentEntity.class));

        LocalDateTime givenDateTime = LocalDateTime.parse(appointmentTime);
        AppointmentEntity testAppointment = new AppointmentEntity(1, 2, 3, givenDateTime);
        appointmentService.create(testAppointment);
    }

}
