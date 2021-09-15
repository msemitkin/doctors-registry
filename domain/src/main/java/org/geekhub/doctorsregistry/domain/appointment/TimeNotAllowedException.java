package org.geekhub.doctorsregistry.domain.appointment;

public class TimeNotAllowedException extends RuntimeException {
    public TimeNotAllowedException(String message) {
        super(message);
    }
}
