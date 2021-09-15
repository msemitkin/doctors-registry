package org.geekhub.doctorsregistry.domain.schedule;


import org.geekhub.doctorsregistry.domain.appointment.appointmenttime.AppointmentTime;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScheduleTest {

    private Schedule schedule;

    @Mock
    private AppointmentTime appointmentTime;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        schedule = new Schedule(appointmentTime);
    }

    @Test
    public void returns_schedule_for_all_seven_days() {
        Mockito.when(appointmentTime.getSupportedTimes())
            .thenReturn(List.of(LocalTime.parse("08:00"), LocalTime.parse("08:20")));
        Assert.assertEquals(schedule.getScheduleMap().size(), 7);
    }

    @Test
    public void returns_schedule_with_all_available_timestamps() {
        List<LocalTime> availableTimestamps = List.of(LocalTime.parse("08:00"), LocalTime.parse("08:20"));
        Mockito.when(appointmentTime.getSupportedTimes()).thenReturn(availableTimestamps);
        Map<DayOfWeek, List<LocalTime>> actual = schedule.getScheduleMap();
        Map<DayOfWeek, List<LocalTime>> expected = Arrays.stream(DayOfWeek.values())
            .collect(Collectors.toMap(dayOfWeek -> dayOfWeek, dayOfWeek -> availableTimestamps));
        Assert.assertEquals(actual, expected);
    }

}