package org.geekhub.doctorsregistry.domain.analytics;

import org.geekhub.doctorsregistry.web.security.role.Role;

import java.util.Map;
import java.util.Objects;

public class UsersAnalyticsEntity {

    private final Map<Role, Integer> analytics;

    public UsersAnalyticsEntity(Map<Role, Integer> analytics) {
        this.analytics = analytics;
    }

    public Map<Role, Integer> getAnalytics() {
        return analytics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersAnalyticsEntity that = (UsersAnalyticsEntity) o;
        return Objects.equals(analytics, that.analytics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(analytics);
    }

    @Override
    public String toString() {
        return "AnalyticsEntity{" +
               "analytics=" + analytics +
               '}';
    }
}
