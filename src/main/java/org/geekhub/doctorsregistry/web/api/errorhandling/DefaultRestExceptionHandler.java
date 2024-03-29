package org.geekhub.doctorsregistry.web.api.errorhandling;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.appointment.DoctorNotAvailableException;
import org.geekhub.doctorsregistry.domain.appointment.PatientBusyException;
import org.geekhub.doctorsregistry.domain.appointment.RepeatedDayAppointmentException;
import org.geekhub.doctorsregistry.domain.appointment.TimeNotAllowedException;
import org.geekhub.doctorsregistry.domain.patient.OperationNotAllowedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(annotations = RestController.class)
public class DefaultRestExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(DefaultRestExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> entityNotFound(EntityNotFoundException e) {
        logger.error("Entity was not found", e);
        return new ResponseEntity<>(
            ErrorDTO.withMessage(
                "Requested entity does not exist"),
            HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DoctorNotAvailableException.class)
    public ResponseEntity<Object> doctorNotAvailable(DoctorNotAvailableException e, WebRequest webRequest) {
        logger.info("Doctor is not available at this time", e);
        return new ResponseEntity<>(
            ErrorDTO.withMessage(
                "Doctor is not available at this time"),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(PatientBusyException.class)
    public ResponseEntity<ErrorDTO> patientBusy(PatientBusyException e) {
        logger.info("Patient busy", e);
        return new ResponseEntity<>(
            ErrorDTO.withMessage(
                "Sorry, you already have an appointment at selected time"),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(RepeatedDayAppointmentException.class)
    public ResponseEntity<ErrorDTO> repeatedDayAppointment(RepeatedDayAppointmentException e) {
        logger.warn("Attempt to make multiple appointments with a doctor on a single day");
        return new ResponseEntity<>(
            ErrorDTO.withMessage(
                "Sorry, you already have an appointment with this doctor on selected day"),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(TimeNotAllowedException.class)
    public ResponseEntity<ErrorDTO> timeNotAllowed(TimeNotAllowedException e) {
        logger.warn("Received appointment with not allowed time", e);
        return new ResponseEntity<>(
            ErrorDTO.withMessage(
                "Sorry, you can only create appointments for the next seven days"),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<ErrorWithStatusDTO> notAllowedOperation() {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        return new ResponseEntity<>(
            new ErrorWithStatusDTO(httpStatus, httpStatus.getReasonPhrase()), httpStatus);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMapDTO handleValidationExceptions(BindException ex) {
        logger.warn("Failed validation: ", ex);
        Map<String, ErrorDTO> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, ErrorDTO.withMessage(errorMessage));
        });
        return new ErrorMapDTO(errors);
    }

    @ExceptionHandler(Exception.class)
    public ErrorWithStatusDTO handleException(Exception e) {
        logger.warn("Unexpected exception: ", e);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ErrorWithStatusDTO(httpStatus, httpStatus.getReasonPhrase());
    }

}
