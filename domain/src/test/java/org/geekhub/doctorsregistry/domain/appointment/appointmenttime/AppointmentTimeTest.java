package org.geekhub.doctorsregistry.domain.appointment.appointmenttime;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalTime;
import java.util.List;

public class AppointmentTimeTest {

    private AppointmentTime appointmentTime;

    @BeforeMethod
    private void setUp() {
        appointmentTime = new AppointmentTime();
    }

    @DataProvider(name = "returns_false_when_time_is_not_valid_parameters")
    private Object[][] returns_false_when_time_is_not_valid_parameters() {
        return new Object[][]{
            {"00:00"},
            {"07:00"},
            {"07:20"},
            {"07:40"},
            {"07:59"},
            {"08:01"},
            {"08:10"},
            {"20:00"},
            {"20:20"},
            {"20:40"}
        };
    }

    @Test(dataProvider = "returns_false_when_time_is_not_valid_parameters")
    public void returns_false_when_time_is_not_valid(String timeString) {
        LocalTime time = LocalTime.parse(timeString);
        Assert.assertFalse(appointmentTime.isTimeValid(time));
    }

    @DataProvider(name = "returns_true_when_time_is_valid_parameters")
    public Object[][] returns_true_when_time_is_valid_parameters() {
        return new Object[][]{
            {"08:00"},
            {"08:20"},
            {"08:40"},
            {"19:00"},
            {"19:20"},
            {"19:40"}
        };
    }

    @Test(dataProvider = "returns_true_when_time_is_valid_parameters")
    public void returns_true_when_time_is_valid(String timeString) {
        LocalTime time = LocalTime.parse(timeString);
        Assert.assertTrue(appointmentTime.isTimeValid(time));
    }

    @DataProvider(name = "returns_all_supported_timestamps_parameters")
    public Object[][] returns_all_supported_timestamps_parameters() {
        return new Object[][]{
            {"08:00"}, {"08:20"}, {"08:40"}, {"19:00"}, {"19:20"}, {"19:40"}
        };
    }

    @Test(dataProvider = "returns_all_supported_timestamps_parameters")
    public void returns_all_supported_timestamps(String stringTime) {
        List<LocalTime> supportedTimes = appointmentTime.getSupportedTimes();
        LocalTime time = LocalTime.parse(stringTime);
        Assert.assertTrue(supportedTimes.contains(time));
    }

    @DataProvider(name = "returns_only_supported_timestamps_parameters")
    public Object[][] returns_only_supported_timestamps_parameters() {
        return new Object[][]{
            {"07:00"}, {"07:20"}, {"07:40"}, {"20:00"}, {"20:20"}, {"20:40"}
        };
    }

    @Test(dataProvider = "returns_only_supported_timestamps_parameters")
    public void returns_only_supported_timestamps(String stringTime) {
        LocalTime time = LocalTime.parse(stringTime);
        List<LocalTime> supportedTimes = appointmentTime.getSupportedTimes();
        Assert.assertFalse(supportedTimes.contains(time));
    }

}