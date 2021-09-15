package org.geekhub.doctorsregistry.api.errorhandling;

public class ErrorDTO {
    private final String message;

    public ErrorDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static ErrorDTO withMessage(String message) {
        return new ErrorDTO(message);
    }
}
