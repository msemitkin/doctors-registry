package org.geekhub.doctorsregistry.repository.appointment;

import org.geekhub.doctorsregistry.repository.DatabaseException;
import org.geekhub.doctorsregistry.repository.SQLManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

@Repository
public class AppointmentRepository {

    private static final RowMapper<AppointmentEntity> rowMapper = (rs, rowNum) ->
        new AppointmentEntity(
            rs.getInt("id"),
            rs.getInt("patient_id"),
            rs.getInt("doctor_id"),
            LocalDateTime.of(
                rs.getDate("date").toLocalDate(),
                rs.getTime("date").toLocalTime()
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

    public Optional<AppointmentEntity> findById(Integer id) {
        try {
            String query = sqlManager.getQuery("find-appointment-by-id");
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(query, Map.of("id", id), rowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
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
                    "doctor_id", doctorId,
                    "time", Time.valueOf(time),
                    "day_of_the_week", dayOfWeek.getValue()
                ),
                Integer.class
                )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public void create(AppointmentEntity appointmentEntity) {

        Integer workingHourId = getWorkingHourId(
            appointmentEntity.getDoctorId(),
            appointmentEntity.getDateTime().toLocalTime(),
            appointmentEntity.getDateTime().getDayOfWeek()
        ).orElseThrow(
            () -> new IllegalArgumentException("Doctor working hour was not found with parameters")
        );

        jdbcTemplate.update(sqlManager.getQuery("save-appointment"), Map.of(
            "patient_id", appointmentEntity.getPatientId(),
            "doctor_working_hour_id", workingHourId,
            "date", Date.valueOf(appointmentEntity.getDateTime().toLocalDate())
            )
        );

    }

    private boolean doctorWorksAt(Integer doctorId, DayOfWeek dayOfWeek, LocalTime localTime) {
        Map<String, ?> parameters = Map.of(
            "doctor_id", doctorId,
            "day_of_the_week", dayOfWeek.getValue(),
            "time", Time.valueOf(localTime)
        );
        return Optional.ofNullable(
            jdbcTemplate.queryForObject(
                sqlManager.getQuery("if-doctor-works-at-day-and-time"),
                parameters,
                Boolean.class
            )
        ).orElse(false);
    }

    private boolean doNotHaveAppointments(Integer doctorId, LocalDateTime dateTime) {
        String query = sqlManager.getQuery("if-doctor-do-not-have-appointment-at");
        Map<String, ?> parameters = Map.of(
            "doctor_id", doctorId,
            "day_of_the_week", dateTime.getDayOfWeek().getValue(),
            "time", Time.valueOf(dateTime.toLocalTime()),
            "date", Date.valueOf(dateTime.toLocalDate())
        );
        return Optional.ofNullable(
            jdbcTemplate.queryForObject(query, parameters, Boolean.class)
        ).orElse(false);
    }

    public boolean doctorAvailable(Integer doctorId, LocalDateTime dateTime) {
        return doctorWorksAt(doctorId, dateTime.getDayOfWeek(), dateTime.toLocalTime()) &&
               doNotHaveAppointments(doctorId, dateTime);
    }

    public boolean patientDoNotHaveAppointment(Integer patientId, LocalDateTime dateTime) {
        String query = sqlManager.getQuery("if-patient-has-appointment-at");
        Map<String, ?> parameters = Map.of(
            "patient_id", patientId,
            "date", Date.valueOf(dateTime.toLocalDate()),
            "time", Time.valueOf(dateTime.toLocalTime())
        );
        boolean patientHasAppointment = Optional.ofNullable(
            jdbcTemplate.queryForObject(query, parameters, Boolean.class))
            .orElseThrow(() -> new DatabaseException(
                    "Expected boolean value while fetching data from db, but null received"
                )
            );
        return !patientHasAppointment;
    }

    public boolean patientHasAppointmentWithThatDoctorThatDay(
        Integer patientId, Integer doctorId, LocalDateTime dateTime
    ) {
        String query = sqlManager.getQuery(
            "if-patient-has-appointment-with-doctor-on-day"
        );
        Map<String, Object> parameters = Map.of(
            "patient_id", patientId,
            "doctor_id", doctorId,
            "date", Date.valueOf(dateTime.toLocalDate()),
            "time", Time.valueOf(dateTime.toLocalTime())
        );
        Boolean result = jdbcTemplate.queryForObject(query, parameters, Boolean.class);
        return Optional.ofNullable(result).orElseThrow(DatabaseException::new);
    }
}
