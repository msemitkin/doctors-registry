package org.geekhub.doctorsregistry.api.mapper;

import org.geekhub.doctorsregistry.api.dto.appointment.AppointmentDTO;
import org.geekhub.doctorsregistry.api.dto.appointment.CreateAppointmentDTO;
import org.geekhub.doctorsregistry.domain.appointment.AppointmentEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppointmentMapper {

    public AppointmentEntity toEntity(CreateAppointmentDTO appointmentDTO) {
        LocalDateTime dateTime = LocalDateTime.parse(appointmentDTO.getInputDateTime());
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setDoctorId(appointmentDTO.getDoctorId());
        appointmentEntity.setDateTime(dateTime);
        return appointmentEntity;
    }

    public AppointmentDTO toDTO(AppointmentEntity appointmentEntity) {
        return new AppointmentDTO(
            appointmentEntity.getId(),
            appointmentEntity.getPatientId(),
            appointmentEntity.getDoctorId(),
            appointmentEntity.getDateTime().toString()
        );
    }
}
