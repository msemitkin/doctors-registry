package org.geekhub.doctorsregistry.repository.doctor;

import org.geekhub.doctorsregistry.domain.datime.ZonedTime;
import org.geekhub.doctorsregistry.repository.DatabaseException;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.util.SQLManager;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.geekhub.doctorsregistry.repository.DatabaseFields.DATE;
import static org.geekhub.doctorsregistry.repository.DatabaseFields.DAY_OF_THE_WEEK;
import static org.geekhub.doctorsregistry.repository.DatabaseFields.DOCTOR_ID;
import static org.geekhub.doctorsregistry.repository.DatabaseFields.ID;
import static org.geekhub.doctorsregistry.repository.DatabaseFields.PATIENT_ID;
import static org.geekhub.doctorsregistry.repository.DatabaseFields.TIME;

@Repository
public class DoctorJdbcTemplateRepository {

    private static final RowMapper<AppointmentEntity> rowMapper = (resultSet, rowNum) -> {
        int id = resultSet.getInt(ID);
        int patientId = resultSet.getInt(PATIENT_ID);
        int doctorId = resultSet.getInt(DOCTOR_ID);
        Date date = resultSet.getDate(DATE);
        Time time = resultSet.getTime(TIME);
        LocalDateTime dateTime = LocalDateTime.of(date.toLocalDate(), time.toLocalTime());
        return new AppointmentEntity(id, patientId, doctorId, dateTime);
    };

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SQLManager sqlManager;

    public DoctorJdbcTemplateRepository(
        NamedParameterJdbcTemplate jdbcTemplate,
        SQLManager sqlManager
    ) {
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

    public List<AppointmentEntity> getAppointments(Integer doctorId) {
        String query = sqlManager.getQuery("get-doctor-appointments");
        return jdbcTemplate.query(query, Map.of(DOCTOR_ID, doctorId), rowMapper);
    }

    public int getIdByEmail(String email) {
        String query = sqlManager.getQuery("get-doctor-id-by-email");
        Integer id = jdbcTemplate.queryForObject(query, Map.of("email", email), Integer.class);
        return Optional.ofNullable(id).orElseThrow(DatabaseException::new);
    }

}
