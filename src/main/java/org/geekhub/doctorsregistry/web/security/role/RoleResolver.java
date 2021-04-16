package org.geekhub.doctorsregistry.web.security.role;

import org.geekhub.doctorsregistry.web.dto.clinic.CreateClinicUserDTO;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.geekhub.doctorsregistry.web.dto.patient.CreatePatientUserDTO;
import org.geekhub.doctorsregistry.web.dto.user.CreateUserDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class RoleResolver {

    public Role resolveRole(CreateUserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException();
        }
        if (userDTO instanceof CreateDoctorUserDTO) {
            return Role.DOCTOR;
        } else if (userDTO instanceof CreateClinicUserDTO) {
            return Role.CLINIC;
        } else if (userDTO instanceof CreatePatientUserDTO) {
            return Role.PATIENT;
        } else {
            throw new RoleNotSupportedException();
        }
    }

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
