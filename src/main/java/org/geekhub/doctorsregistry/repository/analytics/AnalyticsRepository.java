package org.geekhub.doctorsregistry.repository.analytics;

import org.geekhub.doctorsregistry.domain.analytics.UsersAnalyticsEntity;
import org.geekhub.doctorsregistry.repository.util.SQLManager;
import org.geekhub.doctorsregistry.web.security.role.Role;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Repository
public class AnalyticsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SQLManager sqlManager;

    private final RowMapper<SingleRowUsersAnalyticsEntity> analyticsRowMapper = (resultSet, rowNum) -> {
        String userType = resultSet.getString("user_type");
        Role role = Role.from(userType);
        Integer count = resultSet.getInt("count");
        return new SingleRowUsersAnalyticsEntity(role, count);
    };

    public AnalyticsRepository(
        NamedParameterJdbcTemplate jdbcTemplate,
        SQLManager sqlManager
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.sqlManager = sqlManager;
    }

    public UsersAnalyticsEntity getAnalytics() {
        String query = sqlManager.getQuery("get-users-analytics");
        List<SingleRowUsersAnalyticsEntity> singleRowAnalyticsEntities =
            jdbcTemplate.query(query, analyticsRowMapper);
        Map<Role, Integer> analytics = new EnumMap<>(Role.class);
        for (SingleRowUsersAnalyticsEntity analyticsEntity : singleRowAnalyticsEntities) {
            analytics.put(analyticsEntity.role(), analyticsEntity.count());
        }
        return new UsersAnalyticsEntity(analytics);
    }
}

record SingleRowUsersAnalyticsEntity(Role role, int count) {
}
