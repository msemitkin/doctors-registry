package org.geekhub.doctorsregistry.repository.doctorworkinghour;

import org.geekhub.doctorsregistry.repository.DatabaseException;
import org.geekhub.doctorsregistry.repository.SQLManager;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

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
        String query = sqlManager.getQuery("add-working-hour.sql");
        Map<String, ?> parameters = Map.of(
            "doctor_id", doctorWorkingHourEntity.doctorId,
            "time", doctorWorkingHourEntity.time,
            "day_of_the_week", doctorWorkingHourEntity.dayOfTheWeek
        );
        if (!recordExists(parameters)) {
            jdbcTemplate.update(query, parameters);
        }
    }

    private boolean recordExists(Map<String, ?> parameters) {
        String query = sqlManager.getQuery("if-doctor-working-hour-exists");
        Optional<Integer> result = Optional.ofNullable(
            jdbcTemplate.queryForObject(query, parameters, Integer.class)
        );
        return 1 == result.orElseThrow(() -> new DatabaseException("Got a null value"));
    }
}
