package org.geekhub.doctorsregistry.web.security;

import org.geekhub.doctorsregistry.repository.doctor.DoctorRepository;
import org.geekhub.doctorsregistry.web.security.role.Role;
import org.geekhub.doctorsregistry.web.security.role.RoleResolver;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UsernameExtractor {

    private final RoleResolver roleResolver;
    private final DoctorRepository doctorRepository;

    public UsernameExtractor(RoleResolver roleResolver, DoctorRepository doctorRepository) {
        this.roleResolver = roleResolver;
        this.doctorRepository = doctorRepository;
    }

    @Deprecated
    public String getClinicUserName() {
        return getCurrentUserWithRole(Role.CLINIC);
    }

    @Deprecated
    public String getDoctorUserName() {
        return getCurrentUserWithRole(Role.DOCTOR);
    }

    public Integer getDoctorId() {
        String doctorEmail = getCurrentUserWithRole(Role.DOCTOR);
        return doctorRepository.getIdByEmail(doctorEmail);
    }

    @Deprecated
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
