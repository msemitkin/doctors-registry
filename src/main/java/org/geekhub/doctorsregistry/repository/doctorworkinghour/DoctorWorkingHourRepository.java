package org.geekhub.doctorsregistry.repository.doctorworkinghour;

import org.geekhub.doctorsregistry.repository.DatabaseException;
import org.geekhub.doctorsregistry.repository.util.SQLManager;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.geekhub.doctorsregistry.repository.DatabaseFields.*;

@Repository
public class DoctorWorkingHourRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SQLManager sqlManager;

    public DoctorWorkingHourRepository(
        NamedParameterJdbcTemplate jdbcTemplate,
        SQLManager sqlManager
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.sqlManager = sqlManager;
    }


    public void setDoctorWorkingHours(List<DoctorWorkingHourEntity> workingHours) {
        String query = "INSERT INTO doctor_working_hour (doctor_id, day_of_the_week, time) VALUES (:doctor_id, :day_of_the_week, :time)";
        List<SqlParameterSource> parameters = new ArrayList<>();
        for (DoctorWorkingHourEntity doctorWorkingHour : workingHours) {
            Map<String, ?> workingHourMap = toMap(doctorWorkingHour);
            parameters.add(new MapSqlParameterSource(workingHourMap));
        }
        jdbcTemplate.batchUpdate(query, parameters.toArray(new SqlParameterSource[0]));
    }

    private Map<String, ?> toMap(DoctorWorkingHourEntity doctorWorkingHour) {
        return Map.of(
            DOCTOR_ID, doctorWorkingHour.doctorId,
            DAY_OF_THE_WEEK, doctorWorkingHour.dayOfTheWeek,
            TIME, doctorWorkingHour.getTime()
        );
    }

    public void add(DoctorWorkingHourEntity doctorWorkingHourEntity) {
        String query = sqlManager.getQuery("add-working-hour");
        Map<String, ?> parameters = Map.of(
            DOCTOR_ID, doctorWorkingHourEntity.doctorId,
            TIME, doctorWorkingHourEntity.time,
            DAY_OF_THE_WEEK, doctorWorkingHourEntity.dayOfTheWeek
        );
        if (!recordExists(parameters)) {
            jdbcTemplate.update(query, parameters);
        }
    }

    private boolean recordExists(Map<String, ?> parameters) {
        String query = sqlManager.getQuery("if-doctor-working-hour-exists");
        Optional<Boolean> result = Optional.ofNullable(
            jdbcTemplate.queryForObject(query, parameters, Boolean.class)
        );
        return result.orElseThrow(() -> new DatabaseException("Got a null value"));
    }
}
