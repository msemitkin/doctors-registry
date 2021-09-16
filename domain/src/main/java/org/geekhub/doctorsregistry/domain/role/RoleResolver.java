package org.geekhub.doctorsregistry.domain.role;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class RoleResolver {

    public Role resolveRole(UserDetails user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLINIC"))) {
            return Role.CLINIC;
        } else if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DOCTOR"))) {
            return Role.DOCTOR;
        } else if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PATIENT"))) {
            return Role.PATIENT;
        } else if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return Role.ADMIN;
        } else {
            throw new RoleNotSupportedException();
        }
    }
}
