package org.geekhub.doctorsregistry.web.api.analytics;

import org.geekhub.doctorsregistry.domain.analytics.AnalyticsService;
import org.geekhub.doctorsregistry.domain.mapper.UsersAnalyticsMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final UsersAnalyticsMapper analyticsMapper;

    public AnalyticsController(
        AnalyticsService analyticsService,
        UsersAnalyticsMapper analyticsMapper
    ) {
        this.analyticsService = analyticsService;
        this.analyticsMapper = analyticsMapper;
    }

    @GetMapping("/api/users/analytics")
    public UsersAnalyticsDTO getUserAnalytics() {
        return analyticsMapper.toDTO(analyticsService.getUsersAnalytics());
    }

}
