package org.geekhub.doctorsregistry.domain.appointment;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.appointment.appointmenttime.AppointmentTime;
import org.geekhub.doctorsregistry.domain.datime.ZonedTime;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentRepository;
import org.geekhub.doctorsregistry.repository.patient.PatientRepository;
import org.geekhub.doctorsregistry.repository.patient.PatientServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    private final ZonedTime zonedTime;
    private final AppointmentRepository appointmentRepository;
    private final PatientServiceRepository patientRepository;

    public AppointmentService(
        ZonedTime zonedTime,
        AppointmentRepository appointmentRepository,
        PatientServiceRepository patientRepository) {
        this.zonedTime = zonedTime;
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    public void create(User user, Integer doctorId, LocalDateTime dateTime) {
        Integer patientId = patientRepository.getPatientId(user.getUsername());
        AppointmentEntity appointment = new AppointmentEntity(null, patientId, doctorId, dateTime);
        create(appointment);

    }

    public void create(AppointmentEntity appointmentEntity) {

        Assert.notNull(appointmentEntity, "Appointment entity is null");
        Assert.notNull(appointmentEntity.getDoctorId(), "Doctor id is null");
        Assert.notNull(appointmentEntity.getPatientId(), "Patient id is null");
        Assert.notNull(appointmentEntity.getDateTime(), "Datetime is null");

        if (!zonedTime.now().toLocalDate().isBefore(appointmentEntity.getDateTime().toLocalDate())) {
            logger.warn("Cannot create appointment for past time");
            throw new IllegalArgumentException("Received appointment with now allowed time");
        }
        if (!patientDoNotHaveAppointment(appointmentEntity)) {
            logger.warn("Patient already has an appointment at specified time");
            throw new PatientBusyException("Patient already has an appointment at specified time");
        }
        if (patientHasAppointmentWithDoctorThatDay(appointmentEntity)) {
            throw new RepeatedDayAppointment();
        }
        if (!doctorAvailable(appointmentEntity.getDoctorId(), appointmentEntity.getDateTime())) {
            logger.info("Attempted to make an appointment with a doctor at already booked time");
            throw new DoctorNotAvailableException("Doctor is not available at this time");
        }
        appointmentRepository.create(appointmentEntity);
    }

    public AppointmentEntity findById(Integer id) {
        return appointmentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    private boolean doctorAvailable(Integer doctorId, LocalDateTime localDateTime) {
        return appointmentRepository.doctorAvailable(doctorId, localDateTime);
    }

    private boolean patientDoNotHaveAppointment(AppointmentEntity appointmentEntity) {
        return appointmentRepository.patientDoNotHaveAppointment(
            appointmentEntity.getPatientId(), appointmentEntity.getDateTime()
        );
    }

    private boolean patientHasAppointmentWithDoctorThatDay(AppointmentEntity appointmentEntity) {
        return appointmentRepository.patientHasAppointmentWithThatDoctorThatDay(
            appointmentEntity.getPatientId(),
            appointmentEntity.getDoctorId(),
            appointmentEntity.getDateTime()
        );
    }

    private List<LocalTime> getAvailableWorkingHours(Integer doctorId, LocalDate date) {
        List<LocalTime> result = new ArrayList<>();
        List<LocalTime> supportedTimes = AppointmentTime.getSupportedTimes();
        for (LocalTime time : supportedTimes) {
            if (doctorAvailable(doctorId, LocalDateTime.of(date, time))) {
                result.add(time);
            }
        }
        return result;
    }

    public Map<LocalDate, List<LocalTime>> getSchedule(Integer doctorId) {
        Map<LocalDate, List<LocalTime>> result = new TreeMap<>();
        LocalDate dateNow = zonedTime.now().toLocalDate();
        for (int deltaDays = 1; deltaDays < 8; deltaDays++) {
            LocalDate date = dateNow.plusDays(deltaDays);
            List<LocalTime> availableWorkingHours = getAvailableWorkingHours(doctorId, date);
            result.put(date, availableWorkingHours);
        }
        return result;
    }

}
