package org.geekhub.doctorsregistry.web.security;

import org.geekhub.doctorsregistry.repository.clinic.ClinicRepository;
import org.geekhub.doctorsregistry.repository.doctor.DoctorRepository;
import org.geekhub.doctorsregistry.repository.patient.PatientRepository;
import org.geekhub.doctorsregistry.web.security.role.Role;
import org.geekhub.doctorsregistry.web.security.role.RoleResolver;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UsernameExtractor {

    private final RoleResolver roleResolver;
    private final DoctorRepository doctorRepository;
    private final ClinicRepository clinicRepository;
    private final PatientRepository patientRepository;

    public UsernameExtractor(
        RoleResolver roleResolver,
        DoctorRepository doctorRepository,
        ClinicRepository clinicRepository,
        PatientRepository patientRepository
    ) {
        this.roleResolver = roleResolver;
        this.doctorRepository = doctorRepository;
        this.clinicRepository = clinicRepository;
        this.patientRepository = patientRepository;
    }

    public Integer getClinicId() {
        String clinicEmail = getCurrentUserWithRole(Role.CLINIC);
        return clinicRepository.getIdByEmail(clinicEmail);
    }

    public Integer getDoctorId() {
        String doctorEmail = getCurrentUserWithRole(Role.DOCTOR);
        return doctorRepository.getIdByEmail(doctorEmail);
    }

    public Integer getPatientId() {
        String patientEmail = getCurrentUserWithRole(Role.PATIENT);
        return patientRepository.getIdByEmail(patientEmail);
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
