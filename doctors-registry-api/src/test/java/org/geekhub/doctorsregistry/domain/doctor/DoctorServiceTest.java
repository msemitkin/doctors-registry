package org.geekhub.doctorsregistry.domain.doctor;

import org.geekhub.doctorsregistry.domain.datime.ZonedTime;
import org.geekhub.doctorsregistry.domain.mapper.DoctorMapper;
import org.geekhub.doctorsregistry.domain.schedule.DayTimeSpliterator;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.clinic.ClinicRepository;
import org.geekhub.doctorsregistry.repository.doctor.DoctorJdbcTemplateRepository;
import org.geekhub.doctorsregistry.repository.doctor.DoctorRepository;
import org.geekhub.doctorsregistry.repository.doctorworkinghour.DoctorWorkingHourRepository;
import org.geekhub.doctorsregistry.web.security.UsernameExtractor;
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

public class DoctorServiceTest {

    private static final LocalDateTime TEST_TIME = LocalDateTime.parse("2021-10-10T10:00");
    private static final int TEST_DOCTOR_ID = 1;

    private static final List<AppointmentEntity> PAST_APPOINTMENTS = List.of(
        AppointmentEntity.of(1, TEST_DOCTOR_ID, TEST_TIME.minusMinutes(1)),
        AppointmentEntity.of(2, TEST_DOCTOR_ID, TEST_TIME.minusHours(1)),
        AppointmentEntity.of(3, TEST_DOCTOR_ID, TEST_TIME.minusDays(1)),
        AppointmentEntity.of(4, TEST_DOCTOR_ID, TEST_TIME.minusMonths(1))
    );
    private static final List<AppointmentEntity> FUTURE_APPOINTMENTS = List.of(
        AppointmentEntity.of(1, TEST_DOCTOR_ID, TEST_TIME.plusSeconds(1)),
        AppointmentEntity.of(2, TEST_DOCTOR_ID, TEST_TIME.plusMinutes(1)),
        AppointmentEntity.of(3, TEST_DOCTOR_ID, TEST_TIME.plusHours(1)),
        AppointmentEntity.of(4, TEST_DOCTOR_ID, TEST_TIME.plusDays(1))
    );

    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private DoctorJdbcTemplateRepository doctorJdbcTemplateRepository;
    @Mock
    private ZonedTime zonedTime;
    @Mock
    private DayTimeSpliterator dayTimeSpliterator;
    @Mock
    private UserService userService;
    @Mock
    private DoctorMapper doctorMapper;
    @Mock
    private DoctorWorkingHourRepository doctorWorkingHourRepository;
    @Mock
    private ClinicRepository clinicRepository;
    @Mock
    private UsernameExtractor usernameExtractor;

    private DoctorService doctorService;

    @BeforeMethod
    private void setUp() {
        MockitoAnnotations.openMocks(this);
        doctorService = new DoctorService(doctorRepository, doctorJdbcTemplateRepository,
            zonedTime, dayTimeSpliterator, userService, doctorMapper, doctorWorkingHourRepository, clinicRepository, usernameExtractor);
    }

    @Test
    public void returns_empty_list_when_there_are_no_found_appointments() {
        String doctorEmail = "email@gmail.com";
        Mockito.when(doctorJdbcTemplateRepository.getAppointments(TEST_DOCTOR_ID))
            .thenReturn(Collections.emptyList());
        Mockito.when(usernameExtractor.getDoctorUserName()).thenReturn(doctorEmail);
        Mockito.when(doctorRepository.getIdByEmail(doctorEmail)).thenReturn(TEST_DOCTOR_ID);
        Assert.assertTrue(doctorService.getPendingAppointments().isEmpty());
    }

    @Test
    public void returns_empty_list_when_all_found_appointments_have_already_occurred() {
        String doctorEmail = "email@gmail.com";
        Mockito.when(zonedTime.now()).thenReturn(TEST_TIME);
        Mockito.when(doctorJdbcTemplateRepository.getAppointments(TEST_DOCTOR_ID))
            .thenReturn(PAST_APPOINTMENTS);
        Mockito.when(usernameExtractor.getDoctorUserName()).thenReturn(doctorEmail);
        Mockito.when(doctorRepository.getIdByEmail(doctorEmail)).thenReturn(TEST_DOCTOR_ID);
        Assert.assertTrue(doctorService.getPendingAppointments().isEmpty());
    }

    @Test
    public void list_contains_all_future_appointments() {
        String doctorEmail = "email@gmail.com";
        Mockito.when(zonedTime.now()).thenReturn(TEST_TIME);
        Mockito.when(doctorJdbcTemplateRepository.getAppointments(TEST_DOCTOR_ID))
            .thenReturn(FUTURE_APPOINTMENTS);
        Mockito.when(usernameExtractor.getDoctorUserName()).thenReturn(doctorEmail);
        Mockito.when(doctorRepository.getIdByEmail(doctorEmail)).thenReturn(TEST_DOCTOR_ID);
        List<AppointmentEntity> foundFutureAppointments = doctorService.getPendingAppointments();

        Assert.assertEquals(foundFutureAppointments, FUTURE_APPOINTMENTS);
    }

    @Test
    public void returns_only_future_appointments() {
        String doctorEmail = "email@gmail.com";
        List<AppointmentEntity> allAppointments = new ArrayList<>(PAST_APPOINTMENTS);
        allAppointments.addAll(FUTURE_APPOINTMENTS);

        Mockito.when(zonedTime.now()).thenReturn(TEST_TIME);
        Mockito.when(doctorJdbcTemplateRepository.getAppointments(TEST_DOCTOR_ID))
            .thenReturn(allAppointments);
        Mockito.when(usernameExtractor.getDoctorUserName()).thenReturn(doctorEmail);
        Mockito.when(doctorRepository.getIdByEmail(doctorEmail)).thenReturn(TEST_DOCTOR_ID);
        List<AppointmentEntity> foundFutureAppointments = doctorService.getPendingAppointments();

        Assert.assertEquals(foundFutureAppointments, FUTURE_APPOINTMENTS);

    }

    @Test
    public void returns_empty_list_when_there_are_no_appointments() {
        String doctorEmail = "email@gmail.com";
        Mockito.when(zonedTime.now()).thenReturn(TEST_TIME);
        Mockito.when(doctorJdbcTemplateRepository.getAppointments(TEST_DOCTOR_ID)).thenReturn(Collections.emptyList());
        Mockito.when(usernameExtractor.getDoctorUserName()).thenReturn(doctorEmail);
        Mockito.when(doctorRepository.getIdByEmail(doctorEmail)).thenReturn(TEST_DOCTOR_ID);
        List<AppointmentEntity> pastAppointments = doctorService.getArchivedAppointments();
        Assert.assertTrue(pastAppointments.isEmpty());
    }

    @Test
    public void returns_empty_list_when_there_are_no_past_appointments() {
        String doctorEmail = "email@gmail.com";
        Mockito.when(zonedTime.now()).thenReturn(TEST_TIME);
        Mockito.when(doctorJdbcTemplateRepository.getAppointments(TEST_DOCTOR_ID)).thenReturn(Collections.emptyList());
        Mockito.when(usernameExtractor.getDoctorUserName()).thenReturn(doctorEmail);
        Mockito.when(doctorRepository.getIdByEmail(doctorEmail)).thenReturn(TEST_DOCTOR_ID);
        List<AppointmentEntity> pastAppointments = doctorService.getPendingAppointments();
        Assert.assertTrue(pastAppointments.isEmpty());
    }

    @Test
    public void returns_all_past_appointments() {
        String doctorEmail = "email@gmail.com";
        Mockito.when(zonedTime.now()).thenReturn(TEST_TIME);
        Mockito.when(doctorJdbcTemplateRepository.getAppointments(TEST_DOCTOR_ID))
            .thenReturn(PAST_APPOINTMENTS);
        Mockito.when(usernameExtractor.getDoctorUserName()).thenReturn(doctorEmail);
        Mockito.when(doctorRepository.getIdByEmail(doctorEmail)).thenReturn(TEST_DOCTOR_ID);
        List<AppointmentEntity> foundPastAppointments = doctorService.getArchivedAppointments();

        Assert.assertEquals(foundPastAppointments, PAST_APPOINTMENTS);

    }

    @Test
    public void returns_only_past_appointments() {
        String doctorEmail = "email@gmail.com";
        List<AppointmentEntity> allAppointments = new ArrayList<>(PAST_APPOINTMENTS);
        allAppointments.addAll(FUTURE_APPOINTMENTS);

        Mockito.when(zonedTime.now()).thenReturn(TEST_TIME);
        Mockito.when(doctorJdbcTemplateRepository.getAppointments(TEST_DOCTOR_ID))
            .thenReturn(allAppointments);
        Mockito.when(usernameExtractor.getDoctorUserName()).thenReturn(doctorEmail);
        Mockito.when(doctorRepository.getIdByEmail(doctorEmail)).thenReturn(TEST_DOCTOR_ID);
        List<AppointmentEntity> foundPastAppointments = doctorService.getArchivedAppointments();

        Assert.assertEquals(foundPastAppointments, PAST_APPOINTMENTS);
    }

}