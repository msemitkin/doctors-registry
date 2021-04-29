package org.geekhub.doctorsregistry.web.mvc.controller.errorhandling;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.appointment.DoctorNotAvailableException;
import org.geekhub.doctorsregistry.domain.appointment.PatientBusyException;
import org.geekhub.doctorsregistry.domain.appointment.RepeatedDayAppointmentException;
import org.geekhub.doctorsregistry.domain.appointment.TimeNotAllowedException;
import org.geekhub.doctorsregistry.domain.patient.OperationNotAllowedException;
import org.geekhub.doctorsregistry.web.api.errorhandling.ErrorWithStatusDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(annotations = Controller.class)
public class DefaultMVCExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMVCExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String entityNotFound(EntityNotFoundException e, Model model) {
        logger.error("Entity was not found", e);
        model.addAttribute("error", new ErrorWithStatusDTO(HttpStatus.NOT_FOUND,
            "Requested entity does not exist")
        );
        return "error";
    }

    @ExceptionHandler(DoctorNotAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String doctorNotAvailable(DoctorNotAvailableException e, Model model) {
        logger.info("Doctor is not available at this time", e);
        model.addAttribute("error", new ErrorWithStatusDTO(HttpStatus.BAD_REQUEST,
            "Doctor is not available at this time"));
        return "error";
    }

    @ExceptionHandler(PatientBusyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String patientBusy(PatientBusyException e, Model model) {
        logger.info("Patient busy", e);
        model.addAttribute("error", new ErrorWithStatusDTO(HttpStatus.BAD_REQUEST,
            "Sorry, you already have an appointment at selected time"));
        return "error";
    }

    @ExceptionHandler(RepeatedDayAppointmentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String repeatedDayAppointment(RepeatedDayAppointmentException e, Model model) {
        logger.warn("Attempt to make multiple appointments with a doctor on a single day");
        model.addAttribute("error", new ErrorWithStatusDTO(HttpStatus.BAD_REQUEST,
            "Sorry, you already have an appointment with this doctor on selected day"));
        return "error";
    }

    @ExceptionHandler(TimeNotAllowedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String timeNotAllowed(TimeNotAllowedException e, Model model) {
        logger.warn("Received appointment with not allowed time", e);
        model.addAttribute("error", new ErrorWithStatusDTO(HttpStatus.BAD_REQUEST,
            "Sorry, you can only create appointments for the next seven days"));
        return "error";
    }

    @ExceptionHandler(OperationNotAllowedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String notAllowedOperation(OperationNotAllowedException e, Model model) {
        logger.warn("Not allowed operation", e);
        model.addAttribute("error", new ErrorWithStatusDTO(HttpStatus.BAD_REQUEST,
            "Not allowed operation"));
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.warn("Unexpected exception: ", e);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        model.addAttribute("error", new ErrorWithStatusDTO(httpStatus, httpStatus.getReasonPhrase()));
        return "error";
    }
}
