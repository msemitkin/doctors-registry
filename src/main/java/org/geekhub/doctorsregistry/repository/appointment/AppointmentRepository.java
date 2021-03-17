package org.geekhub.doctorsregistry.repository.appointment;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class AppointmentRepository {

    private static final String FIND_BY_ID =
        "select appointment.id as id, " +
        "appointment.patient_id as patient_id, " +
        "doctor_working_hour.doctor_id as doctor_id, " +
        "appointment.datetime as datetime " +
        "from appointment " +
        "join doctor_working_hour " +
        "on appointment.doctor_working_hour_id = doctor_working_hour.id " +
        "where appointment.id = :id";

    private static final String GET_DOCTOR_WORKING_HOUR_ID_BY_DOCTOR_ID_AND_DAY_OF_THE_WEEK_AND_TIME =
        "select doctor_working_hour.id as id " +
        "from doctor_working_hour join working_hour " +
        "on doctor_working_hour.working_hour_id = working_hour.id " +
        "where doctor_working_hour.doctor_id = :doctor_id " +
        "and working_hour.time = :time " +
        "and UPPER(working_hour.day_of_the_week) = :day_of_the_week ";

    private static final String CREATE_APPOINTMENT =
        "insert into appointment (patient_id, doctor_working_hour_id, datetime) " +
        "values (:patient_id, :doctor_working_hour_id, :datetime)";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AppointmentRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<AppointmentEntity> rowMapper = (rs, rowNum) -> new AppointmentEntity(
        rs.getInt("id"),
        rs.getInt("patient_id"),
        rs.getInt("doctor_id"),
        rs.getTimestamp("datetime").toLocalDateTime()
    );

    public Optional<AppointmentEntity> findById(Integer id) {
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(FIND_BY_ID, Map.of("id", id), rowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public void create(AppointmentEntity appointmentEntity) {
        Integer workingHourId = jdbcTemplate.queryForObject(
            GET_DOCTOR_WORKING_HOUR_ID_BY_DOCTOR_ID_AND_DAY_OF_THE_WEEK_AND_TIME,
            Map.of(
                "doctor_id", appointmentEntity.getDoctorId(),
                "time", appointmentEntity.getDateTime().toLocalTime(),
                "day_of_the_week", appointmentEntity.getDateTime().getDayOfWeek().name()
            ),
            Integer.class
        );

        if (Objects.isNull(workingHourId)) {
            throw new IllegalArgumentException("Requested entity does not exist");
        }

        jdbcTemplate.update(CREATE_APPOINTMENT, Map.of(
            "patient_id", appointmentEntity.getPatientId(),
            "doctor_working_hour_id", workingHourId,
            "datetime", Timestamp.valueOf(appointmentEntity.getDateTime())
            )
        );

    }

}
