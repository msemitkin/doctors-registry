package org.geekhub.doctorsregistry.web.security;

import org.geekhub.doctorsregistry.web.security.role.Role;
import org.geekhub.doctorsregistry.web.security.role.RoleResolver;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UsernameExtractor {

    private final RoleResolver roleResolver;

    public UsernameExtractor(RoleResolver roleResolver) {
        this.roleResolver = roleResolver;
    }

    public String getClinicUserName() {
        return getCurrentUserWithRole(Role.CLINIC);
    }

    public String getDoctorUserName() {
        return getCurrentUserWithRole(Role.DOCTOR);
    }

    public String getPatientUsername() {
        return getCurrentUserWithRole(Role.PATIENT);
    }


    private String getCurrentUserWithRole(Role role) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        if (roleResolver.resolveRole(userDetails).equals(role)) {
            return userDetails.getUsername();
        } else {
            throw new UserRoleMismatchException();
        }
    }
}
