package org.geekhub.doctorsregistry.domain.mapper;

import org.geekhub.doctorsregistry.domain.analytics.UsersAnalyticsEntity;
import org.geekhub.doctorsregistry.web.api.analytics.UsersAnalyticsDTO;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UsersAnalyticsMapper {

    public UsersAnalyticsDTO toDTO(UsersAnalyticsEntity usersAnalyticsEntity) {
        Map<String, Integer> analytics =
            usersAnalyticsEntity.getAnalytics().entrySet().stream()
                .collect(
                    Collectors.toMap(
                        entry -> entry.getKey().name(),
                        Map.Entry::getValue)
                );
        return new UsersAnalyticsDTO(analytics);
    }
}
