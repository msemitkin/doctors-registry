package org.geekhub.doctorsregistry.repository.patient;

import org.geekhub.doctorsregistry.repository.DatabaseException;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.util.SQLManager;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.geekhub.doctorsregistry.repository.DatabaseFields.*;

@Repository
public class PatientJdbcTemplateRepository {

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

    public PatientJdbcTemplateRepository(
        NamedParameterJdbcTemplate jdbcTemplate,
        SQLManager sqlManager
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.sqlManager = sqlManager;
    }

    public Integer getPatientId(String email) {
        String query = sqlManager.getQuery("get-patient-id-by-email");
        return jdbcTemplate.queryForObject(query, Map.of("email", email), Integer.class);
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

    public List<AppointmentEntity> getAppointments(Integer patientId) {
        String query = sqlManager.getQuery("get-patient-appointments");
        return jdbcTemplate.query(query, Map.of(PATIENT_ID, patientId), rowMapper);
    }

}
