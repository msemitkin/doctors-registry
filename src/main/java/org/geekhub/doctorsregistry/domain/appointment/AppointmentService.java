package org.geekhub.doctorsregistry.domain.appointment;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public AppointmentEntity findById(Integer id) {
        return appointmentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<AppointmentEntity> findByDoctorId(Integer doctorId) {
        return appointmentRepository.findAppointmentEntitiesByDoctorId(doctorId);
    }

    public List<AppointmentEntity> findByPatientId(Integer patientId) {
        return appointmentRepository.findAppointmentEntitiesByPatientId(patientId);
    }

    public AppointmentEntity save(AppointmentEntity appointmentEntity) {
        return appointmentRepository.save(appointmentEntity);
    }

}
