package org.geekhub.doctorsregistry.domain.appointment;

public class DoctorNotAvailableException extends RuntimeException {
    public DoctorNotAvailableException(String message) {
        super(message);
    }
}
