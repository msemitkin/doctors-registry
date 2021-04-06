package org.geekhub.doctorsregistry.domain.patient;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.patient.PatientEntity;
import org.geekhub.doctorsregistry.repository.patient.PatientJdbcTemplateRepository;
import org.geekhub.doctorsregistry.repository.patient.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientJdbcTemplateRepository patientJdbcTemplateRepository;

    public PatientService(PatientRepository patientRepository, PatientJdbcTemplateRepository patientJdbcTemplateRepository) {
        this.patientRepository = patientRepository;
        this.patientJdbcTemplateRepository = patientJdbcTemplateRepository;
    }

    public PatientEntity save(PatientEntity patientEntity) {
        return patientRepository.save(patientEntity);
    }

    public PatientEntity findById(Integer id) {
        return patientRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public boolean patientHasAppointmentOnSelectedTime(AppointmentEntity appointmentEntity) {
        return !patientJdbcTemplateRepository.patientDoNotHaveAppointment(
            appointmentEntity.getPatientId(), appointmentEntity.getDateTime()
        );
    }

    public boolean patientHasAppointmentWithDoctorThatDay(AppointmentEntity appointmentEntity) {
        return patientJdbcTemplateRepository.patientHasAppointmentWithThatDoctorThatDay(
            appointmentEntity.getPatientId(),
            appointmentEntity.getDoctorId(),
            appointmentEntity.getDateTime()
        );
    }

    public void deleteById(Integer id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id);
        }
    }
}
