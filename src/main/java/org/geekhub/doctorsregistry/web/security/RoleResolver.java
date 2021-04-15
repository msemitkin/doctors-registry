package org.geekhub.doctorsregistry.web.security;

import org.geekhub.doctorsregistry.web.dto.clinic.CreateClinicUserDTO;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.geekhub.doctorsregistry.web.dto.user.CreateUserDTO;
import org.geekhub.doctorsregistry.web.mvc.controller.RoleNotSupportedException;
import org.geekhub.doctorsregistry.web.mvc.controller.user.RegisterPatientDTO;
import org.springframework.stereotype.Component;

@Component
public class RoleResolver {

    public String[] resolveRoles(CreateUserDTO userDTO) {
        if (userDTO instanceof CreateDoctorUserDTO) {
            return new String[]{"DOCTOR"};
        } else if (userDTO instanceof CreateClinicUserDTO) {
            return new String[]{"CLINIC"};
        } else if (userDTO instanceof RegisterPatientDTO) {
            return new String[]{"PATIENT"};
        } else {
            throw new RoleNotSupportedException();
        }
    }
}
