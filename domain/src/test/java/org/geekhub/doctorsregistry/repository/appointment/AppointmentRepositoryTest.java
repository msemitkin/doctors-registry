package org.geekhub.doctorsregistry.repository.appointment;

import org.geekhub.doctorsregistry.domain.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.util.SQLManagerConfig;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

@JdbcTest
@ContextConfiguration(classes = {AppointmentRepository.class, SQLManagerConfig.class})
public class AppointmentRepositoryTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeMethod
    public void setUp() {
        JdbcTestUtils.deleteFromTables(namedParameterJdbcTemplate.getJdbcTemplate(), "appointment");
    }

    @Test
    public void dependencies_are_not_null() {
        Assertions.assertNotNull(appointmentRepository);
    }

    @Test
    public void result_is_empty_when_there_is_no_appointment_with_given_id() {
        Optional<AppointmentEntity> actual = appointmentRepository.findById(1);
        Assert.assertTrue(actual.isEmpty());
    }

}