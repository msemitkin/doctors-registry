package org.geekhub.doctorsregistry.domain.appointment;

import org.assertj.core.api.Assertions;
import org.geekhub.doctorsregistry.domain.appointment.appointmenttime.AppointmentTime;
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
    @Mock
    private AppointmentTime appointmentTime;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        appointmentValidator = new AppointmentValidator(zonedTime, doctorService, patientService, appointmentTime);
    }

    @Test
    public void allows_appointment_with_null_id() {
        AppointmentEntity appointmentEntity = AppointmentEntity.create(1, 1, TEST_DATE_TIME);

        Mockito.when(zonedTime.now()).thenReturn(TEST_DATE_TIME.minusDays(1));
        Mockito.when(patientService.patientHasAppointmentOnSelectedTime(Mockito.any())).thenReturn(false);
        Mockito.when(patientService.patientHasAppointmentWithDoctorThatDay(Mockito.any())).thenReturn(false);
        Mockito.when(doctorService.isDoctorAvailable(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(appointmentTime.isTimeValid(appointmentEntity.getDateTime().toLocalTime())).thenReturn(true);

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
    public void works_correct_with_correct_datetime(String scheduledTime) {
        LocalDateTime givenDateTime = LocalDateTime.parse(scheduledTime);

        Mockito.when(zonedTime.now()).thenReturn(TEST_DATE_TIME);
        Mockito.when(appointmentTime.isTimeValid(givenDateTime.toLocalTime())).thenReturn(true);

        Assert.assertFalse(appointmentValidator.isDateTimeNotAllowed(givenDateTime));
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
    public void throws_exception_when_create_appointment_for_not_allowed_time(String timeNow, String scheduledTime) {
        LocalDateTime givenDateTime = LocalDateTime.parse(scheduledTime);

        Mockito.when(zonedTime.now()).thenReturn(LocalDateTime.parse(timeNow));
        Mockito.when(appointmentTime.isTimeValid(givenDateTime.toLocalTime())).thenReturn(true);

        Assert.assertTrue(appointmentValidator.isDateTimeNotAllowed(givenDateTime));
    }

    @Test
    public void throws_PatientBusyException_when_patient_already_has_an_appointment_for_specified_time() {
        AppointmentEntity appointmentEntity = AppointmentEntity.create(1, 2, LocalDateTime.parse("2021-10-10T10:20"));

        Mockito.when(zonedTime.now()).thenReturn(LocalDateTime.parse("2021-10-08T10:20"));
        Mockito.when(patientService.patientHasAppointmentOnSelectedTime(appointmentEntity)).thenReturn(true);
        Mockito.when(patientService.patientHasAppointmentWithDoctorThatDay(appointmentEntity)).thenReturn(false);
        Mockito.when(doctorService.isDoctorAvailable(appointmentEntity.getDoctorId(), appointmentEntity.getDateTime())).thenReturn(true);
        Mockito.when(appointmentTime.isTimeValid(appointmentEntity.getDateTime().toLocalTime())).thenReturn(true);

        Assertions.assertThatCode(() -> appointmentValidator.validate(appointmentEntity))
            .isInstanceOf(PatientBusyException.class);
    }

    @Test
    public void throws_RepeatedDayAppointmentException_when_patient_already_has_an_appointment_with_chosen_doctor_on_specified_day() {
        AppointmentEntity appointmentEntity = AppointmentEntity.create(1, 2, LocalDateTime.parse("2021-10-10T10:20"));
        Mockito.when(zonedTime.now()).thenReturn(LocalDateTime.parse("2021-10-08T10:20"));
        Mockito.when(patientService.patientHasAppointmentOnSelectedTime(appointmentEntity)).thenReturn(false);
        Mockito.when(patientService.patientHasAppointmentWithDoctorThatDay(appointmentEntity)).thenReturn(true);
        Mockito.when(doctorService.isDoctorAvailable(appointmentEntity.getDoctorId(), appointmentEntity.getDateTime())).thenReturn(true);
        Mockito.when(appointmentTime.isTimeValid(appointmentEntity.getDateTime().toLocalTime())).thenReturn(true);

        Assertions.assertThatCode(() -> appointmentValidator.validate(appointmentEntity))
            .isInstanceOf(RepeatedDayAppointmentException.class);
    }

    @Test
    public void throws_DoctorNotAvailableException_when_doctor_is_not_available() {
        AppointmentEntity appointmentEntity = AppointmentEntity.create(1, 2, LocalDateTime.parse("2021-10-10T10:20"));
        Mockito.when(zonedTime.now()).thenReturn(LocalDateTime.parse("2021-10-08T10:20"));
        Mockito.when(patientService.patientHasAppointmentOnSelectedTime(appointmentEntity)).thenReturn(false);
        Mockito.when(patientService.patientHasAppointmentWithDoctorThatDay(appointmentEntity)).thenReturn(false);
        Mockito.when(doctorService.isDoctorAvailable(appointmentEntity.getDoctorId(), appointmentEntity.getDateTime())).thenReturn(false);
        Mockito.when(appointmentTime.isTimeValid(appointmentEntity.getDateTime().toLocalTime())).thenReturn(true);
        Assertions.assertThatCode(() -> appointmentValidator.validate(appointmentEntity))
            .isInstanceOf(DoctorNotAvailableException.class);
    }

    @Test
    public void do_not_throw_any_exceptions_when_given_valid_data_and_it_is_allowed_to_create_appointment() {
        AppointmentEntity appointmentEntity = AppointmentEntity.create(1, 2, LocalDateTime.parse("2021-10-10T10:20"));
        Mockito.when(zonedTime.now()).thenReturn(LocalDateTime.parse("2021-10-08T10:20"));
        Mockito.when(patientService.patientHasAppointmentOnSelectedTime(appointmentEntity)).thenReturn(false);
        Mockito.when(patientService.patientHasAppointmentWithDoctorThatDay(appointmentEntity)).thenReturn(false);
        Mockito.when(doctorService.isDoctorAvailable(appointmentEntity.getDoctorId(), appointmentEntity.getDateTime())).thenReturn(true);
        Mockito.when(appointmentTime.isTimeValid(appointmentEntity.getDateTime().toLocalTime())).thenReturn(true);
        Assertions.assertThatCode(() -> appointmentValidator.validate(appointmentEntity))
            .doesNotThrowAnyException();
    }
}