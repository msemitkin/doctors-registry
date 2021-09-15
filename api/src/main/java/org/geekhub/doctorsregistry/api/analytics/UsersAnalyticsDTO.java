package org.geekhub.doctorsregistry.api.analytics;

import java.util.Map;

public class UsersAnalyticsDTO {
    private final Map<String, Integer> analytics;

    public UsersAnalyticsDTO(Map<String, Integer> analytics) {
        this.analytics = analytics;
    }

    public Map<String, Integer> getAnalytics() {
        return analytics;
    }
}
