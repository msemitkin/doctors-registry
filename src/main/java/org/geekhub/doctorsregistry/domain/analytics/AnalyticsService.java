package org.geekhub.doctorsregistry.domain.analytics;

import org.geekhub.doctorsregistry.repository.analytics.AnalyticsRepository;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    private final AnalyticsRepository analyticsRepository;

    public AnalyticsService(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    public UsersAnalyticsEntity getUsersAnalytics() {
        return analyticsRepository.getAnalytics();
    }
}
