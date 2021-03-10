package org.geekhub.doctorsregistry.mapper;

import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.geekhub.doctorsregistry.repository.patient.PatientEntity;
import org.geekhub.doctorsregistry.web.appointment.AppointmentDTO;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    private final PatientMapper patientMapper;
    private final DoctorMapper doctorMapper;

    public AppointmentMapper(PatientMapper patientMapper, DoctorMapper doctorMapper) {
        this.patientMapper = patientMapper;
        this.doctorMapper = doctorMapper;
    }

    public AppointmentDTO toDTO(AppointmentEntity entity) {
        DoctorEntity doctorEntity = entity.getDoctorWorkingHour().getDoctorEntity();
        PatientEntity patientEntity = entity.getPatient();
        return new AppointmentDTO(
            entity.getId(),
            patientMapper.toDTO(patientEntity),
            doctorMapper.toDTO(doctorEntity),
            entity.getDateTime()
        );
    }
}
