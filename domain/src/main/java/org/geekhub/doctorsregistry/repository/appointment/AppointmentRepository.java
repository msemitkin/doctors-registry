package org.geekhub.doctorsregistry.repository.appointment;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.util.SQLManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

import static org.geekhub.doctorsregistry.repository.DatabaseFields.*;

@Repository
public class AppointmentRepository {

    private static final RowMapper<AppointmentEntity> rowMapper = (rs, rowNum) ->
        new AppointmentEntity(
            rs.getInt(ID),
            rs.getInt(PATIENT_ID),
            rs.getInt(DOCTOR_ID),
            LocalDateTime.of(
                rs.getDate(DATE).toLocalDate(),
                rs.getTime(TIME).toLocalTime()
            )
        );

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SQLManager sqlManager;

    public AppointmentRepository(
        NamedParameterJdbcTemplate jdbcTemplate,
        SQLManager sqlManager
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.sqlManager = sqlManager;
    }

    @NonNull
    public AppointmentEntity findById(Integer id) {
        try {
            String query = sqlManager.getQuery("find-appointment-by-id");
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(query, Map.of(ID, id), rowMapper)
            ).orElseThrow(EntityNotFoundException::new);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException();
        }
    }

    public void deleteById(Integer id) {
        String query = sqlManager.getQuery("delete-appointment-by-id");
        jdbcTemplate.update(query, Map.of(ID, id));
    }

    private Optional<Integer> getWorkingHourId(
        Integer doctorId,
        LocalTime time,
        DayOfWeek dayOfWeek
    ) {
        String query = sqlManager.getQuery(
            "find-doctor-working-hour-id-by-doctor-id-and-day-of-the-week-and-time"
        );
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    query,
                    Map.of(
                        DOCTOR_ID, doctorId,
                        TIME, Time.valueOf(time),
                        DAY_OF_THE_WEEK, dayOfWeek.getValue()
                    ),
                    Integer.class
                )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void create(AppointmentEntity appointmentEntity) {

        Integer workingHourId = getWorkingHourId(
            appointmentEntity.getDoctorId(),
            appointmentEntity.getDateTime().toLocalTime(),
            appointmentEntity.getDateTime().getDayOfWeek()
        ).orElseThrow(
            () -> new IllegalArgumentException("Doctor working hour was not found with parameters")
        );

        jdbcTemplate.update(sqlManager.getQuery("save-appointment"), Map.of(
                PATIENT_ID, appointmentEntity.getPatientId(),
                DOCTOR_WORKING_HOUR_ID, workingHourId,
                DATE, Date.valueOf(appointmentEntity.getDateTime().toLocalDate())
            )
        );

    }

}
