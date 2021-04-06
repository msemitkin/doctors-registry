package org.geekhub.doctorsregistry.repository.doctorworkinghour;

import org.geekhub.doctorsregistry.repository.DatabaseException;
import org.geekhub.doctorsregistry.repository.util.SQLManager;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

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
