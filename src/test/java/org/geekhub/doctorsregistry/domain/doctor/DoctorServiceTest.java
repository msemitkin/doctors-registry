package org.geekhub.doctorsregistry.domain.doctor;

import org.geekhub.doctorsregistry.domain.datime.ZonedTime;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.clinic.ClinicRepository;
import org.geekhub.doctorsregistry.repository.doctor.DoctorJdbcTemplateRepository;
import org.geekhub.doctorsregistry.repository.doctor.DoctorRepository;
import org.geekhub.doctorsregistry.repository.doctorworkinghour.DoctorWorkingHourRepository;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class DoctorServiceTest {

    private static final LocalDateTime TEST_TIME = LocalDateTime.parse("2021-10-10T10:00");
    private static final int TEST_DOCTOR_ID = 1;

    private static final List<AppointmentEntity> PAST_APPOINTMENTS = List.of(
        AppointmentEntity.create(1, TEST_DOCTOR_ID, TEST_TIME.minusMinutes(1)),
        AppointmentEntity.create(2, TEST_DOCTOR_ID, TEST_TIME.minusHours(1)),
        AppointmentEntity.create(3, TEST_DOCTOR_ID, TEST_TIME.minusDays(1)),
        AppointmentEntity.create(4, TEST_DOCTOR_ID, TEST_TIME.minusMonths(1))
    );
    private static final List<AppointmentEntity> FUTURE_APPOINTMENTS = List.of(
        AppointmentEntity.create(1, TEST_DOCTOR_ID, TEST_TIME.plusSeconds(1)),
        AppointmentEntity.create(2, TEST_DOCTOR_ID, TEST_TIME.plusMinutes(1)),
        AppointmentEntity.create(3, TEST_DOCTOR_ID, TEST_TIME.plusHours(1)),
        AppointmentEntity.create(4, TEST_DOCTOR_ID, TEST_TIME.plusDays(1))
    );

    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private DoctorJdbcTemplateRepository doctorJdbcTemplateRepository;
    @Mock
    private ZonedTime zonedTime;
    @Mock
    private UserService userService;
    @Mock
    private DoctorWorkingHourRepository doctorWorkingHourRepository;
    @Mock
    private ClinicRepository clinicRepository;

    private DoctorService doctorService;

    @BeforeMethod
    private void setUp() {
        MockitoAnnotations.openMocks(this);
        doctorService = new DoctorService(doctorRepository, doctorJdbcTemplateRepository,
            zonedTime, userService, doctorWorkingHourRepository, clinicRepository);
    }

    @Test
    public void returns_empty_list_when_there_are_no_found_appointments() {
        when(doctorJdbcTemplateRepository.getAppointments(TEST_DOCTOR_ID)).thenReturn(Collections.emptyList());

        Assert.assertTrue(doctorService.getPendingAppointments(TEST_DOCTOR_ID).isEmpty());
    }

    @Test
    public void returns_empty_list_when_all_found_appointments_have_already_occurred() {
        when(zonedTime.now()).thenReturn(TEST_TIME);
        when(doctorJdbcTemplateRepository.getAppointments(TEST_DOCTOR_ID)).thenReturn(PAST_APPOINTMENTS);

        Assert.assertTrue(doctorService.getPendingAppointments(TEST_DOCTOR_ID).isEmpty());
    }

    @Test
    public void list_contains_all_future_appointments() {
        when(zonedTime.now()).thenReturn(TEST_TIME);
        when(doctorJdbcTemplateRepository.getAppointments(TEST_DOCTOR_ID)).thenReturn(FUTURE_APPOINTMENTS);

        List<AppointmentEntity> foundFutureAppointments = doctorService.getPendingAppointments(TEST_DOCTOR_ID);

        Assert.assertEquals(foundFutureAppointments, FUTURE_APPOINTMENTS);
    }

    @Test
    public void returns_only_future_appointments() {
        List<AppointmentEntity> allAppointments = new ArrayList<>(PAST_APPOINTMENTS);
        allAppointments.addAll(FUTURE_APPOINTMENTS);
        when(zonedTime.now()).thenReturn(TEST_TIME);
        when(doctorJdbcTemplateRepository.getAppointments(TEST_DOCTOR_ID)).thenReturn(allAppointments);

        List<AppointmentEntity> foundFutureAppointments = doctorService.getPendingAppointments(TEST_DOCTOR_ID);

        Assert.assertEquals(foundFutureAppointments, FUTURE_APPOINTMENTS);

    }

    @Test
    public void returns_empty_list_when_there_are_no_appointments() {
        when(zonedTime.now()).thenReturn(TEST_TIME);
        when(doctorJdbcTemplateRepository.getAppointments(TEST_DOCTOR_ID)).thenReturn(Collections.emptyList());

        List<AppointmentEntity> pastAppointments = doctorService.getArchivedAppointments(TEST_DOCTOR_ID);

        Assert.assertTrue(pastAppointments.isEmpty());
    }

    @Test
    public void returns_empty_list_when_there_are_no_past_appointments() {
        when(zonedTime.now()).thenReturn(TEST_TIME);
        when(doctorJdbcTemplateRepository.getAppointments(TEST_DOCTOR_ID)).thenReturn(Collections.emptyList());

        List<AppointmentEntity> pastAppointments = doctorService.getPendingAppointments(TEST_DOCTOR_ID);

        Assert.assertTrue(pastAppointments.isEmpty());
    }

    @Test
    public void returns_all_past_appointments() {
        when(zonedTime.now()).thenReturn(TEST_TIME);
        when(doctorJdbcTemplateRepository.getAppointments(TEST_DOCTOR_ID)).thenReturn(PAST_APPOINTMENTS);

        List<AppointmentEntity> foundPastAppointments = doctorService.getArchivedAppointments(TEST_DOCTOR_ID);

        Assert.assertEquals(foundPastAppointments, PAST_APPOINTMENTS);

    }

    @Test
    public void returns_only_past_appointments() {
        List<AppointmentEntity> allAppointments = new ArrayList<>(PAST_APPOINTMENTS);
        allAppointments.addAll(FUTURE_APPOINTMENTS);
        when(zonedTime.now()).thenReturn(TEST_TIME);
        when(doctorJdbcTemplateRepository.getAppointments(TEST_DOCTOR_ID)).thenReturn(allAppointments);

        List<AppointmentEntity> foundPastAppointments = doctorService.getArchivedAppointments(TEST_DOCTOR_ID);

        Assert.assertEquals(foundPastAppointments, PAST_APPOINTMENTS);
    }

    @Test
    public void returns_schedule_for_next_seven_days() {
        LocalDateTime today = LocalDateTime.of(2022, 3, 13, 10, 0);
        when(zonedTime.now()).thenReturn(today);
        LocalTime time = LocalTime.of(10, 0);
        when(doctorWorkingHourRepository.getAvailableWorkingHours(eq(TEST_DOCTOR_ID), any(LocalDate.class)))
            .thenReturn(List.of(time));
        LocalDate todayDate = today.toLocalDate();

        Map<LocalDate, List<LocalTime>> actualSchedule = doctorService.getSchedule(TEST_DOCTOR_ID);

        Map<LocalDate, List<LocalTime>> expectedSchedule = Map.ofEntries(
            entry(todayDate.plusDays(1), List.of(time)),
            entry(todayDate.plusDays(2), List.of(time)),
            entry(todayDate.plusDays(3), List.of(time)),
            entry(todayDate.plusDays(4), List.of(time)),
            entry(todayDate.plusDays(5), List.of(time)),
            entry(todayDate.plusDays(6), List.of(time)),
            entry(todayDate.plusDays(7), List.of(time))
        );
        Assert.assertEquals(actualSchedule, expectedSchedule);
    }

}