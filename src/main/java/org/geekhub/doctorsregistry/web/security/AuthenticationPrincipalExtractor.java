package org.geekhub.doctorsregistry.web.security;

import org.geekhub.doctorsregistry.repository.clinic.ClinicRepository;
import org.geekhub.doctorsregistry.repository.doctor.DoctorRepository;
import org.geekhub.doctorsregistry.repository.patient.PatientRepository;
import org.geekhub.doctorsregistry.web.security.role.Role;
import org.geekhub.doctorsregistry.web.security.role.RoleNotSupportedException;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationPrincipalExtractor {

    private final DoctorRepository doctorRepository;
    private final ClinicRepository clinicRepository;
    private final PatientRepository patientRepository;

    public AuthenticationPrincipalExtractor(
        DoctorRepository doctorRepository,
        ClinicRepository clinicRepository,
        PatientRepository patientRepository
    ) {
        this.doctorRepository = doctorRepository;
        this.clinicRepository = clinicRepository;
        this.patientRepository = patientRepository;
    }

    @NonNull
    public AuthenticationPrincipal getPrincipal() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = Optional
            .ofNullable(securityContext.getAuthentication())
            .orElseThrow(UnauthenticatedException::new);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Role role = resolveRole(userDetails);
        int userId = getUserId(userDetails.getUsername(), role);
        return new AuthenticationPrincipal(userId, role);
    }

    private int getUserId(String email, @NonNull Role role) {
        return switch (role) {
            case DOCTOR -> doctorRepository.getIdByEmail(email);
            case PATIENT -> patientRepository.getIdByEmail(email);
            case CLINIC -> clinicRepository.getIdByEmail(email);
            case ADMIN -> -1;
        };
    }

    @NonNull
    private Role resolveRole(@NonNull UserDetails user) {
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
