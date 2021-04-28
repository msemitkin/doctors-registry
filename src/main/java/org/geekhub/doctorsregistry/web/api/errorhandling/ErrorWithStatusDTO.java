package org.geekhub.doctorsregistry.web.api.errorhandling;

import org.springframework.http.HttpStatus;

public class ErrorWithStatusDTO {

    private HttpStatus status;
    private String message;

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
