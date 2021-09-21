package org.geekhub.doctorsregistry.repository.user;

import org.geekhub.doctorsregistry.repository.util.SQLManager;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SQLManager sqlManager;

    public UserRepositoryImpl(
        NamedParameterJdbcTemplate jdbcTemplate,
        SQLManager sqlManager
    ) {
        this.jdbcTemplate = jdbcTemplate;

        this.sqlManager = sqlManager;
    }

    @Override
    public void createUser(UserDetails user) {
        String query = sqlManager.getQuery("create-user");
        Map<String, ?> parameters = Map.of(
            "email", user.getUsername(),
            "password", user.getPassword(),
            "enabled", user.isEnabled()
        );
        jdbcTemplate.update(query, parameters);
    }

    @Override
    public void updateUser(UserDetails user) {
        String query = sqlManager.getQuery("update-user");
        Map<String, ?> parameters = Map.of(
            "email", user.getUsername(),
            "password", user.getPassword(),
            "enabled", user.isEnabled()
        );
        jdbcTemplate.update(query, parameters);
    }

    @Override
    public void deleteUser(String username) {
        String query = sqlManager.getQuery("delete-user");
        jdbcTemplate.update(query, Map.of("email", username));
    }

    @Override
    public void changePassword(String email, String newPassword) {
        String query = sqlManager.getQuery("change-password");
        jdbcTemplate.update(query, Map.of("email", email, "password", newPassword));
    }

    @Override
    public boolean userExists(String email) {
        String query = sqlManager.getQuery("user-exists");
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, Map.of("email", email), Boolean.class))
            .orElse(true);
    }

    @Override
    public UserDetails loadUserByEmail(String email) {
        String query = sqlManager.getQuery("user-by-email");
        jdbcTemplate.queryForObject(query, Map.of("email", email), this::getUserDetails);
        return null;
    }

    private UserDetails getUserDetails(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
            .username(resultSet.getString("email"))
            .disabled(!resultSet.getBoolean("enabled"))
            .password(resultSet.getString("password"))
            .build();

    }
}
