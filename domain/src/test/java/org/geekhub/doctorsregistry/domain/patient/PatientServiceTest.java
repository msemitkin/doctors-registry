package org.geekhub.doctorsregistry.domain.patient;

import org.geekhub.doctorsregistry.domain.datime.ZonedTime;
import org.geekhub.doctorsregistry.domain.mapper.PatientMapper;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.domain.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.patient.PatientJdbcTemplateRepository;
import org.geekhub.doctorsregistry.repository.patient.PatientRepository;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatientServiceTest {

    private static final LocalDateTime TEST_TIME = LocalDateTime.parse("2021-10-10T10:00");
    private static final int TEST_PATIENT_ID = 1;

    private static final List<AppointmentEntity> PAST_APPOINTMENTS = List.of(
        AppointmentEntity.of(TEST_PATIENT_ID, 1, TEST_TIME.minusMinutes(1)),
        AppointmentEntity.of(TEST_PATIENT_ID, 2, TEST_TIME.minusHours(1)),
        AppointmentEntity.of(TEST_PATIENT_ID, 3, TEST_TIME.minusDays(1)),
        AppointmentEntity.of(TEST_PATIENT_ID, 4, TEST_TIME.minusMonths(1))
    );
    private static final List<AppointmentEntity> FUTURE_APPOINTMENTS = List.of(
        AppointmentEntity.of(TEST_PATIENT_ID, 1, TEST_TIME.plusSeconds(1)),
        AppointmentEntity.of(TEST_PATIENT_ID, 2, TEST_TIME.plusMinutes(1)),
        AppointmentEntity.of(TEST_PATIENT_ID, 3, TEST_TIME.plusHours(1)),
        AppointmentEntity.of(TEST_PATIENT_ID, 4, TEST_TIME.plusDays(1))
    );

    @Mock
    private PatientRepository patientRepository;
    @Mock
    private PatientJdbcTemplateRepository patientJdbcTemplateRepository;
    @Mock
    private ZonedTime zonedTime;
    @Mock
    private UserService userService;
    @Mock
    private PatientMapper patientMapper;

    private PatientService patientService;

    @BeforeMethod
    private void setUp() {
        MockitoAnnotations.openMocks(this);
        patientService = new PatientService(patientRepository, patientJdbcTemplateRepository,
            zonedTime, userService, patientMapper);
    }

    @Test
    public void returns_empty_list_when_there_are_no_found_appointments() {
        Mockito.when(patientJdbcTemplateRepository.getAppointments(TEST_PATIENT_ID))
            .thenReturn(Collections.emptyList());
        Assert.assertTrue(patientService.getPendingAppointments(TEST_PATIENT_ID).isEmpty());
    }

    @Test
    public void returns_empty_list_when_all_found_appointments_have_already_occurred() {

        Mockito.when(zonedTime.now()).thenReturn(TEST_TIME);
        Mockito.when(patientJdbcTemplateRepository.getAppointments(TEST_PATIENT_ID))
            .thenReturn(PAST_APPOINTMENTS);

        Assert.assertTrue(patientService.getPendingAppointments(TEST_PATIENT_ID).isEmpty());
    }

    @Test
    public void list_contains_all_future_appointments() {

        Mockito.when(zonedTime.now()).thenReturn(TEST_TIME);
        Mockito.when(patientJdbcTemplateRepository.getAppointments(TEST_PATIENT_ID))
            .thenReturn(FUTURE_APPOINTMENTS);

        List<AppointmentEntity> foundFutureAppointments = patientService.getPendingAppointments(TEST_PATIENT_ID);

        Assert.assertEquals(foundFutureAppointments, FUTURE_APPOINTMENTS);
    }

    @Test
    public void returns_only_future_appointments() {
        List<AppointmentEntity> allAppointments = new ArrayList<>(PAST_APPOINTMENTS);
        allAppointments.addAll(FUTURE_APPOINTMENTS);

        Mockito.when(zonedTime.now()).thenReturn(TEST_TIME);
        Mockito.when(patientJdbcTemplateRepository.getAppointments(TEST_PATIENT_ID))
            .thenReturn(allAppointments);

        List<AppointmentEntity> foundFutureAppointments = patientService.getPendingAppointments(TEST_PATIENT_ID);

        Assert.assertEquals(foundFutureAppointments, FUTURE_APPOINTMENTS);

    }

    @Test
    public void returns_empty_list_when_there_are_no_appointments() {
        Mockito.when(zonedTime.now()).thenReturn(TEST_TIME);
        Mockito.when(patientJdbcTemplateRepository.getAppointments(TEST_PATIENT_ID))
            .thenReturn(Collections.emptyList());

        List<AppointmentEntity> pastAppointments = patientService.getArchivedAppointments(TEST_PATIENT_ID);
        Assert.assertTrue(pastAppointments.isEmpty());
    }

    @Test
    public void returns_empty_list_when_there_are_no_past_appointments() {
        Mockito.when(zonedTime.now()).thenReturn(TEST_TIME);
        Mockito.when(patientJdbcTemplateRepository.getAppointments(TEST_PATIENT_ID))
            .thenReturn(FUTURE_APPOINTMENTS);

        Assert.assertTrue(patientService.getArchivedAppointments(TEST_PATIENT_ID).isEmpty());
    }

    @Test
    public void returns_all_past_appointments() {
        Mockito.when(zonedTime.now()).thenReturn(TEST_TIME);
        Mockito.when(patientJdbcTemplateRepository.getAppointments(TEST_PATIENT_ID))
            .thenReturn(PAST_APPOINTMENTS);

        List<AppointmentEntity> foundPastAppointments = patientService.getArchivedAppointments(TEST_PATIENT_ID);

        Assert.assertEquals(foundPastAppointments, PAST_APPOINTMENTS);

    }

    @Test
    public void returns_only_past_appointments() {

        List<AppointmentEntity> allAppointments = new ArrayList<>(PAST_APPOINTMENTS);
        allAppointments.addAll(FUTURE_APPOINTMENTS);

        Mockito.when(zonedTime.now()).thenReturn(TEST_TIME);
        Mockito.when(patientJdbcTemplateRepository.getAppointments(TEST_PATIENT_ID))
            .thenReturn(allAppointments);

        List<AppointmentEntity> foundPastAppointments = patientService.getArchivedAppointments(TEST_PATIENT_ID);

        Assert.assertEquals(foundPastAppointments, PAST_APPOINTMENTS);
    }


}