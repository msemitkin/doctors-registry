package org.geekhub.doctorsregistry.domain.appointment;

public class TimeNotAllowed extends RuntimeException {
    public TimeNotAllowed(String message) {
        super(message);
    }
}
