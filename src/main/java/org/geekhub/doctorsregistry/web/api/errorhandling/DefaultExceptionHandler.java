package org.geekhub.doctorsregistry.web.api.errorhandling;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.appointment.DoctorNotAvailableException;
import org.geekhub.doctorsregistry.domain.appointment.PatientBusyException;
import org.geekhub.doctorsregistry.domain.appointment.RepeatedDayAppointment;
import org.geekhub.doctorsregistry.domain.appointment.TimeNotAllowed;
import org.geekhub.doctorsregistry.domain.patient.OperationNotAllowedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> entityNotFound(EntityNotFoundException e) {
        logger.error("Entity was not found", e);
        return new ResponseEntity<>(
            ErrorDTO.withMessage("Requested entity does not exist"),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(DoctorNotAvailableException.class)
    public ResponseEntity<ErrorDTO> doctorNotAvailable(DoctorNotAvailableException e) {
        logger.info("Doctor is not available at this time", e);
        return new ResponseEntity<>(
            ErrorDTO.withMessage("Doctor is not available at this time"),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(PatientBusyException.class)
    public ResponseEntity<ErrorDTO> patientBusy(PatientBusyException e) {
        logger.info("Patient busy", e);
        return new ResponseEntity<>(
            ErrorDTO.withMessage(
                "Sorry, you already have an appointment at selected time"
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(RepeatedDayAppointment.class)
    public ResponseEntity<ErrorDTO> repeatedDayAppointment(RepeatedDayAppointment e) {
        logger.warn("Attempt to make multiple appointments with a doctor on a single day");
        return new ResponseEntity<>(
            ErrorDTO.withMessage(
                "Sorry, you already have an appointment with this doctor on selected day"
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(TimeNotAllowed.class)
    public ResponseEntity<ErrorDTO> timeNotAllowed(TimeNotAllowed e) {
        logger.warn("Received appointment with not allowed time", e);
        return new ResponseEntity<>(ErrorDTO.withMessage(
            "Sorry, you can only create appointments for the next seven days"),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<ErrorDTO> notAllowedOperation(OperationNotAllowedException e) {
        return new ResponseEntity<>(ErrorDTO.withMessage("Not allowed operation"), HttpStatus.BAD_REQUEST);
    }
}
