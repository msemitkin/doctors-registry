package org.geekhub.doctorsregistry.repository.patient;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class PatientServiceRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PatientServiceRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer getPatientId(String email) {
        String query = "SELECT id FROM patient where email = :email";
        return jdbcTemplate.queryForObject(query, Map.of("email", email), Integer.class);
    }
}
