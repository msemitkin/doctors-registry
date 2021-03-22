package org.geekhub.doctorsregistry.repository.appointment;

import org.geekhub.doctorsregistry.repository.DatabaseException;
import org.geekhub.doctorsregistry.repository.util.SQLManager;
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

    private static final String ID = "id";
    private static final String PATIENT_ID = "patient_id";
    private static final String DOCTOR_ID = "doctor_id";
    private static final String DATE = "date";
    private static final String TIME = "time";
    private static final String DAY_OF_THE_WEEK = "day_of_the_week";
    private static final String DOCTOR_WORKING_HOUR_ID = "doctor_working_hour_id";

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

    public Optional<AppointmentEntity> findById(Integer id) {
        try {
            String query = sqlManager.getQuery("find-appointment-by-id");
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(query, Map.of(ID, id), rowMapper)
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
            PATIENT_ID, appointmentEntity.getPatientId(),
            DOCTOR_WORKING_HOUR_ID, workingHourId,
            DATE, Date.valueOf(appointmentEntity.getDateTime().toLocalDate())
            )
        );

    }

    private boolean doctorWorksAt(Integer doctorId, DayOfWeek dayOfWeek, LocalTime localTime) {
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

    private boolean doNotHaveAppointments(Integer doctorId, LocalDateTime dateTime) {
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

    public boolean doctorAvailable(Integer doctorId, LocalDateTime dateTime) {
        return doctorWorksAt(doctorId, dateTime.getDayOfWeek(), dateTime.toLocalTime()) &&
               doNotHaveAppointments(doctorId, dateTime);
    }

    public boolean patientDoNotHaveAppointment(Integer patientId, LocalDateTime dateTime) {
        String query = sqlManager.getQuery("if-patient-has-appointment-at");
        Map<String, ?> parameters = Map.of(
            PATIENT_ID, patientId,
            DATE, Date.valueOf(dateTime.toLocalDate()),
            TIME, Time.valueOf(dateTime.toLocalTime())
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
            PATIENT_ID, patientId,
            DOCTOR_ID, doctorId,
            DATE, Date.valueOf(dateTime.toLocalDate()),
            TIME, Time.valueOf(dateTime.toLocalTime())
        );
        Boolean result = jdbcTemplate.queryForObject(query, parameters, Boolean.class);
        return Optional.ofNullable(result).orElseThrow(DatabaseException::new);
    }
}
