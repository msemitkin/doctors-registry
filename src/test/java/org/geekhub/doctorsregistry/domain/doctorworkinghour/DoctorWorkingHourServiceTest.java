package org.geekhub.doctorsregistry.domain.doctorworkinghour;

import org.geekhub.doctorsregistry.domain.DoctorWorkingHourService;
import org.geekhub.doctorsregistry.repository.doctor.DoctorRepository;
import org.geekhub.doctorsregistry.repository.doctorworkinghour.DoctorWorkingHourEntity;
import org.geekhub.doctorsregistry.repository.doctorworkinghour.DoctorWorkingHourRepository;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.sql.Time;
import java.time.DayOfWeek;

import static org.assertj.core.api.Assertions.assertThatCode;

public class DoctorWorkingHourServiceTest {
    private static final DoctorWorkingHourEntity DOCTOR_WORKING_HOUR_TEST_ENTITY =
        new DoctorWorkingHourEntity(null, 1, Time.valueOf("08:00:00"), DayOfWeek.THURSDAY.getValue());

    @Mock
    private DoctorWorkingHourRepository doctorWorkingHourRepository;

    @Mock
    private DoctorRepository doctorRepository;

    private DoctorWorkingHourService doctorWorkingHourService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        doctorWorkingHourService = new DoctorWorkingHourService(doctorWorkingHourRepository, doctorRepository);
    }

    @Test
    private void do_not_throw_any_exception_when_data_is_correct() {

        Mockito.doNothing().when(doctorWorkingHourRepository).add(DOCTOR_WORKING_HOUR_TEST_ENTITY);
        Mockito.when(doctorRepository.existsById(Mockito.any())).thenReturn(true);

        assertThatCode(
            () -> doctorWorkingHourService.addWorkingHour(DOCTOR_WORKING_HOUR_TEST_ENTITY)
        ).doesNotThrowAnyException();

    }

    @DataProvider(name = "throws_exception_if_given_not_valid_time_parameters")
    private Object[][] throws_exception_if_given_not_valid_time_parameters() {
        return new Object[][]{
            {"08:01:00"}, {"08:10:00"}, {"00:00:00"}, {"-1"}, {"08:59"}, {"08:00:01"}
        };
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
        dataProvider = "throws_exception_if_given_not_valid_time_parameters")
    public void throws_exception_if_given_not_valid_time(String timeString) {
        DoctorWorkingHourEntity testEntity = new DoctorWorkingHourEntity(
            null, 1, Time.valueOf(timeString), DayOfWeek.valueOf("MONDAY").getValue()
        );

        Mockito.doNothing().when(doctorWorkingHourRepository).add(testEntity);
        Mockito.when(doctorRepository.existsById(Mockito.any())).thenReturn(true);

        doctorWorkingHourService.addWorkingHour(testEntity);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void throws_exception_if_doctor_with_given_id_does_not_exist() {

        Mockito.doNothing().when(doctorWorkingHourRepository).add(DOCTOR_WORKING_HOUR_TEST_ENTITY);
        Mockito.when(doctorRepository.existsById(Mockito.any())).thenReturn(false);

        doctorWorkingHourService.addWorkingHour(DOCTOR_WORKING_HOUR_TEST_ENTITY);
    }

}
