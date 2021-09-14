package org.geekhub.doctorsregistry.domain;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException() {
    }

    public EntityNotFoundException(int id) {
        super("Element with id: " + id + " does not exist");
    }
}
