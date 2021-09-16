package org.geekhub.doctorsregistry.domain.analytics;

import org.geekhub.doctorsregistry.domain.role.Role;

import java.util.Objects;

public class SingleRowUsersAnalyticsEntity {

    private final Role role;
    private final int count;

    public SingleRowUsersAnalyticsEntity(Role role, int count) {
        this.role = role;
        this.count = count;
    }

    public Role getRole() {
        return role;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleRowUsersAnalyticsEntity that = (SingleRowUsersAnalyticsEntity) o;
        return role == that.role && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, count);
    }

    @Override
    public String toString() {
        return "SingleRowAnalyticsEntity{" +
               "role=" + role +
               ", count=" + count +
               '}';
    }
}
