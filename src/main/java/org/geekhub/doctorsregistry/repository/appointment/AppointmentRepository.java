package org.geekhub.doctorsregistry.repository.appointment;

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

    private static final String FIND_BY_ID =
        "select appointment.id as id, " +
        "appointment.patient_id as patient_id, " +
        "doctor_working_hour.doctor_id as doctor_id, " +
        "appointment.date as date, " +
        "doctor_working_hour.time as time " +
        "from appointment " +
        "join doctor_working_hour " +
        "on appointment.doctor_working_hour_id = doctor_working_hour.id " +
        "where appointment.id = :id";

    private static final String GET_DOCTOR_WORKING_HOUR_ID_BY_DOCTOR_ID_AND_DAY_OF_THE_WEEK_AND_TIME =
        "select doctor_working_hour.id as id " +
        "from doctor_working_hour " +
        "where doctor_working_hour.doctor_id = :doctor_id " +
        "and doctor_working_hour.time = :time " +
        "and doctor_working_hour.day_of_the_week = :day_of_the_week ";

    private static final String CREATE_APPOINTMENT =
        "insert into appointment (patient_id, doctor_working_hour_id, date) " +
        "values (:patient_id, :doctor_working_hour_id, :date)";

    private static final String DOCTOR_WORKS_AT = """
        select exists( 
               select id 
               from doctor_working_hour 
               where doctor_id = :doctor_id 
                 and day_of_the_week = :day_of_the_week 
                 and time = :time            
        )
        """;


    private static final String DOCTOR_DO_NOT_HAVE_APPOINTMENTS = """
        select not exists(
            select *
            from appointment
                     join doctor_working_hour dwh on appointment.doctor_working_hour_id = dwh.id
            where doctor_id = :doctor_id
              and day_of_the_week = :day_of_the_week
              and time = :time
              and date = :date
        )""";


    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AppointmentRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    public Optional<AppointmentEntity> findById(Integer id) {
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(FIND_BY_ID, Map.of("id", id), rowMapper)
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
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                GET_DOCTOR_WORKING_HOUR_ID_BY_DOCTOR_ID_AND_DAY_OF_THE_WEEK_AND_TIME,
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

        jdbcTemplate.update(CREATE_APPOINTMENT, Map.of(
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
            jdbcTemplate.queryForObject(DOCTOR_WORKS_AT, parameters, Boolean.class)
        ).orElse(false);
    }

    private boolean doNotHaveAppointments(Integer doctorId, LocalDateTime dateTime) {
        Map<String, ?> parameters = Map.of(
            "doctor_id", doctorId,
            "day_of_the_week", dateTime.getDayOfWeek().getValue(),
            "time", Time.valueOf(dateTime.toLocalTime()),
            "date", Date.valueOf(dateTime.toLocalDate())
        );
        return Optional.ofNullable(
            jdbcTemplate.queryForObject(DOCTOR_DO_NOT_HAVE_APPOINTMENTS, parameters, Boolean.class)
        ).orElse(false);
    }

    public boolean doctorAvailable(Integer doctorId, LocalDateTime dateTime) {
        return doctorWorksAt(doctorId, dateTime.getDayOfWeek(), dateTime.toLocalTime()) &&
               doNotHaveAppointments(doctorId, dateTime);
    }

}
