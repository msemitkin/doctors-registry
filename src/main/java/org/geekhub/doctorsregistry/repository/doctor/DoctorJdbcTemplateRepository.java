package org.geekhub.doctorsregistry.repository.doctor;

import org.geekhub.doctorsregistry.repository.util.SQLManager;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

@Repository
public class DoctorJdbcTemplateRepository {

    private static final String DOCTOR_ID = "doctor_id";
    private static final String DATE = "date";
    private static final String TIME = "time";
    private static final String DAY_OF_THE_WEEK = "day_of_the_week";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SQLManager sqlManager;

    public DoctorJdbcTemplateRepository(NamedParameterJdbcTemplate jdbcTemplate, SQLManager sqlManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.sqlManager = sqlManager;
    }

    public boolean doctorWorksAt(Integer doctorId, DayOfWeek dayOfWeek, LocalTime localTime) {
        Map<String, ?> parameters = Map.of(
            DOCTOR_ID, doctorId,
            DAY_OF_THE_WEEK, dayOfWeek.getValue(),
            TIME, Time.valueOf(localTime)
        );
        return Optional.ofNullable(
            jdbcTemplate.queryForObject(
                sqlManager.getQuery("if-doctor-works-at-day-and-time"),
                parameters,
                Boolean.class
            )
        ).orElse(false);
    }

    public boolean doNotHaveAppointments(Integer doctorId, LocalDateTime dateTime) {
        String query = sqlManager.getQuery("if-doctor-do-not-have-appointment-at");
        Map<String, ?> parameters = Map.of(
            DOCTOR_ID, doctorId,
            DAY_OF_THE_WEEK, dateTime.getDayOfWeek().getValue(),
            TIME, Time.valueOf(dateTime.toLocalTime()),
            DATE, Date.valueOf(dateTime.toLocalDate())
        );
        return Optional.ofNullable(
            jdbcTemplate.queryForObject(query, parameters, Boolean.class)
        ).orElse(false);
    }

}
