package org.geekhub.doctorsregistry.domain.mapper;

import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.web.dto.appointment.AppointmentDTO;
import org.geekhub.doctorsregistry.web.dto.appointment.CreateAppointmentDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppointmentMapper {

    public AppointmentEntity toEntity(CreateAppointmentDTO appointmentDTO) {
        LocalDateTime dateTime = LocalDateTime.parse(appointmentDTO.getInputDateTime());
        return AppointmentEntity.of(appointmentDTO.getPatientId(), appointmentDTO.getDoctorId(), dateTime);
    }

    public AppointmentDTO toDTO(AppointmentEntity appointmentEntity) {
        return new AppointmentDTO(
            appointmentEntity.getId(),
            appointmentEntity.getPatientId(),
            appointmentEntity.getPatientId(),
            appointmentEntity.getDateTime()
        );
    }
}
