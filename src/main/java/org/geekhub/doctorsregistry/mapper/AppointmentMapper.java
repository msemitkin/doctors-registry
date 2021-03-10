package org.geekhub.doctorsregistry.mapper;

import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.geekhub.doctorsregistry.repository.patient.PatientEntity;
import org.geekhub.doctorsregistry.web.appointment.AppointmentDTO;
import org.geekhub.doctorsregistry.web.doctor.DoctorDTO;
import org.geekhub.doctorsregistry.web.patient.PatientDTO;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentDTO toDto(AppointmentEntity entity) {
        DoctorEntity doctorEntity = entity.getDoctorWorkingHour().getDoctorEntity();
        PatientEntity patientEntity = entity.getPatient();
        return new AppointmentDTO(
            entity.getId(),
            PatientDTO.of(patientEntity),
            DoctorDTO.of(doctorEntity),
            entity.getDateTime()
        );
    }
}
