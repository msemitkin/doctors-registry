package org.geekhub.doctorsregistry.web.api.appointment;

import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentDTO toDTO(AppointmentEntity entity) {
        return new AppointmentDTO(
            entity.getId(),
            entity.getPatientId(),
            entity.getDoctorId(),
            entity.getDateTime()
        );
    }
}
