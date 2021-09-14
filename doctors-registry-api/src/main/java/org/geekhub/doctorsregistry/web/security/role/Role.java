package org.geekhub.doctorsregistry.web.security.role;

public enum Role {
    ADMIN, CLINIC, DOCTOR, PATIENT;

    public static Role from(String stringRole) {
        if (stringRole == null) {
            throw new IllegalArgumentException();
        }
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(stringRole)) {
                return role;
            }
        }
        throw new RoleNotSupportedException();
    }
}
