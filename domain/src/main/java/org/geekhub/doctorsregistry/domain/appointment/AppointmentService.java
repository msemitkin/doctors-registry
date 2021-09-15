package org.geekhub.doctorsregistry.domain.appointment;

import org.geekhub.doctorsregistry.domain.datime.ZonedTime;
import org.geekhub.doctorsregistry.domain.patient.OperationNotAllowedException;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentRepository;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentValidator appointmentValidator;
    private final ZonedTime zonedTime;

    public AppointmentService(
        AppointmentRepository appointmentRepository,
        AppointmentValidator appointmentValidator,
        ZonedTime zonedTime
    ) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentValidator = appointmentValidator;
        this.zonedTime = zonedTime;
    }

    public void create(CreateAppointmentCommand createAppointmentCommand) {
        AppointmentEntity appointment = new AppointmentEntity(
            null,
            createAppointmentCommand.getPatientId(),
            createAppointmentCommand.getDoctorId(),
            createAppointmentCommand.getAppointmentTime()
        );
        appointmentValidator.validate(appointment);
        appointmentRepository.create(appointment);
    }

    public AppointmentEntity findById(int id) {
        return appointmentRepository.findById(id);
    }

    public void deleteById(Integer appointmentId) {
        AppointmentEntity appointment = appointmentRepository.findById(appointmentId);
        if (isAppointmentPending(appointment)) {
            appointmentRepository.deleteById(appointmentId);
        } else {
            throw new OperationNotAllowedException();
        }
    }

    private boolean isAppointmentPending(AppointmentEntity appointment) {
        return appointment.getDateTime().isAfter(zonedTime.now());
    }

}
