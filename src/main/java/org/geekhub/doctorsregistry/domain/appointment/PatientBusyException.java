package org.geekhub.doctorsregistry.domain.appointment;

public class PatientBusyException extends RuntimeException {

    public PatientBusyException(String message) {
        super(message);
    }
}
