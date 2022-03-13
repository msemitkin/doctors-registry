package org.geekhub.doctorsregistry.web.security.role;

import org.geekhub.doctorsregistry.web.dto.clinic.CreateClinicUserDTO;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.geekhub.doctorsregistry.web.dto.patient.CreatePatientUserDTO;
import org.geekhub.doctorsregistry.web.dto.user.CreateUserDTO;
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
}
