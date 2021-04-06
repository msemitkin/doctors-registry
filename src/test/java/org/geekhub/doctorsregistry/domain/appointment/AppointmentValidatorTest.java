package org.geekhub.doctorsregistry.domain.appointment;

import org.geekhub.doctorsregistry.domain.datime.ZonedTime;
import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.domain.patient.PatientService;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDateTime;

public class AppointmentValidatorTest {
    private static final LocalDateTime TEST_DATE_TIME =
        LocalDateTime.parse("2002-04-17T10:00:00");

    private AppointmentValidator appointmentValidator;

    @Mock
    private ZonedTime zonedTime;
    @Mock
    private PatientService patientService;
    @Mock
    private DoctorService doctorService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        appointmentValidator = new AppointmentValidator(zonedTime, doctorService, patientService);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void throws_exception_when_appointment_is_null() {
        appointmentValidator.validate(null);
    }

    @Test
    public void allows_appointment_with_null_id() {
        AppointmentEntity appointmentEntity = new AppointmentEntity(null, 1, 1, TEST_DATE_TIME);

        Mockito.when(zonedTime.now()).thenReturn(TEST_DATE_TIME.minusDays(1));
        Mockito.when(patientService.patientHasAppointmentOnSelectedTime(Mockito.any())).thenReturn(false);
        Mockito.when(patientService.patientHasAppointmentWithDoctorThatDay(Mockito.any())).thenReturn(false);
        Mockito.when(doctorService.doctorAvailable(Mockito.any(), Mockito.any())).thenReturn(true);

        appointmentValidator.validate(appointmentEntity);
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
        appointmentValidator.validate(appointmentEntity);
    }

    @DataProvider(name = "works_correct_with_correct_datetime_parameters")
    private Object[][] works_correct_with_correct_datetime_parameters() {
        return new Object[][]{
            {"2002-04-18T08:00"},
            {"2002-04-20T19:40"},
            {"2002-04-24T19:40"}
        };
    }

    @Test(dataProvider = "works_correct_with_correct_datetime_parameters")
    public void works_correct_with_correct_datetime(String appointmentTime) {
        Mockito.when(zonedTime.now()).thenReturn(TEST_DATE_TIME);
        LocalDateTime appointmentDate = LocalDateTime.parse(appointmentTime);
        Assert.assertFalse(appointmentValidator.isDateTimeNotAllowed(appointmentDate));
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
            {"2021-03-29T00:00:00", "2021-03-29T14:00:00"},
            {"2021-03-01T10:00:00", "2021-03-09T10:00:00"}
        };
    }

    @Test(dataProvider = "throws_exception_when_create_appointment_for_past_time_parameters")
    public void throws_exception_when_create_appointment_for_not_allowed_time(String timeNow, String appointmentTime) {
        Mockito.when(zonedTime.now()).thenReturn(LocalDateTime.parse(timeNow));

        LocalDateTime givenDateTime = LocalDateTime.parse(appointmentTime);
        Assert.assertTrue(appointmentValidator.isDateTimeNotAllowed(givenDateTime));
    }

}