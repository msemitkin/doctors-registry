package org.geekhub.doctorsregistry.repository.doctorworkinghour;

import org.geekhub.doctorsregistry.repository.DatabaseException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class DoctorWorkingHourRepository {

    private static final String ADD_WORKING_HOUR =
        "insert into doctor_working_hour (doctor_id, time, day_of_the_week) " +
        "values (:doctor_id, :time, :day_of_the_week)";

    private static final String IF_RECORD_EXISTS =
        "select count(1) " +
        "from doctor_working_hour " +
        "where doctor_id = :doctor_id " +
        "and time = :time " +
        "and day_of_the_week = :day_of_the_week";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DoctorWorkingHourRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(DoctorWorkingHourEntity doctorWorkingHourEntity) {
        Map<String, ?> parameters = Map.of(
            "doctor_id", doctorWorkingHourEntity.doctorId,
            "time", doctorWorkingHourEntity.time,
            "day_of_the_week", doctorWorkingHourEntity.dayOfTheWeek
        );
        if (!recordExists(parameters)) {
            jdbcTemplate.update(ADD_WORKING_HOUR, parameters);
        }
    }

    private boolean recordExists(Map<String, ?> parameters) {
        Optional<Integer> result = Optional.ofNullable(
            jdbcTemplate.queryForObject(IF_RECORD_EXISTS, parameters, Integer.class)
        );
        return 1 == result.orElseThrow(() -> new DatabaseException("Got a null value"));
    }
}
