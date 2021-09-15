package org.geekhub.doctorsregistry.api.errorhandling;

import org.springframework.http.HttpStatus;

public class ErrorWithStatusDTO {

    private final HttpStatus status;
    private final String message;

    public ErrorWithStatusDTO(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
