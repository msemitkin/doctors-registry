package org.geekhub.doctorsregistry.domain.appointment;

import org.geekhub.doctorsregistry.domain.appointment.appointmenttime.AppointmentTime;
import org.geekhub.doctorsregistry.domain.datime.ZonedTime;
import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.domain.patient.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AppointmentValidator {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentValidator.class);


    private final ZonedTime zonedTime;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentTime appointmentTime;

    public AppointmentValidator(
        ZonedTime zonedTime,
        DoctorService doctorService,
        PatientService patientService,
        AppointmentTime appointmentTime
    ) {
        this.zonedTime = zonedTime;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.appointmentTime = appointmentTime;
    }

    public void validate(AppointmentEntity appointmentEntity) {

        Assert.notNull(appointmentEntity, "Appointment entity is null");
        Assert.notNull(appointmentEntity.getDoctorId(), "Doctor id is null");
        Assert.notNull(appointmentEntity.getPatientId(), "Patient id is null");
        Assert.notNull(appointmentEntity.getDateTime(), "Datetime is null");

        if (isDateTimeNotAllowed(appointmentEntity.getDateTime())) {
            logger.warn("Not allowed date");
            throw new TimeNotAllowedException("Received appointment with now allowed date: "
                                              + appointmentEntity.getDateTime());
        }
        if (patientService.patientHasAppointmentOnSelectedTime(appointmentEntity)) {
            logger.warn("Patient already has an appointment at specified time");
            throw new PatientBusyException("Patient already has an appointment at specified time");
        }
        if (patientService.patientHasAppointmentWithDoctorThatDay(appointmentEntity)) {
            logger.warn("Second appointment with a single doctor on a specific day");
            throw new RepeatedDayAppointmentException();
        }
        if (!doctorService.doctorAvailable(appointmentEntity.getDoctorId(), appointmentEntity.getDateTime())) {
            logger.info("Attempted to make an appointment with a doctor at already booked time");
            throw new DoctorNotAvailableException("Doctor is not available at this time");
        }

    }

    public boolean isDateTimeNotAllowed(LocalDateTime dateTime) {
        LocalDate currentDate = zonedTime.now().toLocalDate();
        LocalDate appointmentDate = dateTime.toLocalDate();

        boolean timeAllowed = appointmentTime.isTimeValid(dateTime.toLocalTime())
                              && currentDate.isBefore(appointmentDate)
                              && appointmentDate.isBefore(currentDate.plusDays(8));
        return !timeAllowed;
    }

}
