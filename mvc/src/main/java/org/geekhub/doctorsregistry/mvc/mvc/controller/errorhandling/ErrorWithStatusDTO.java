package org.geekhub.doctorsregistry.mvc.mvc.controller.errorhandling;

import org.springframework.http.HttpStatus;

class ErrorWithStatusDTO {

    private final HttpStatus status;
    private final String message;

    ErrorWithStatusDTO(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    HttpStatus getStatus() {
        return status;
    }

    String getMessage() {
        return message;
    }
}
