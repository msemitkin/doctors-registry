package org.geekhub.doctorsregistry.domain.mapper;

import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.web.dto.appointment.AppointmentDTO;
import org.geekhub.doctorsregistry.web.dto.appointment.CreateAppointmentDTO;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppointmentMapper {

    @NonNull
    public AppointmentEntity toEntity(int patientId, @NonNull CreateAppointmentDTO appointmentDTO) {
        LocalDateTime appointmentTime = LocalDateTime.parse(appointmentDTO.getInputDateTime());
        return AppointmentEntity.create(patientId, appointmentDTO.getDoctorId(), appointmentTime);
    }

    @NonNull
    public AppointmentDTO toDTO(@NonNull AppointmentEntity appointmentEntity) {
        return new AppointmentDTO(
            appointmentEntity.getId(),
            appointmentEntity.getPatientId(),
            appointmentEntity.getDoctorId(),
            appointmentEntity.getDateTime().toString()
        );
    }
}
