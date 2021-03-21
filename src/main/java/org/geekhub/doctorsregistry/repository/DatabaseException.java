package org.geekhub.doctorsregistry.repository;

public class DatabaseException extends RuntimeException {

    public DatabaseException() {
    }

    public DatabaseException(String message) {
        super(message);
    }
}
