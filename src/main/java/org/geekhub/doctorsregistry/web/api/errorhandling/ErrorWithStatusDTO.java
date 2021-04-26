package org.geekhub.doctorsregistry.web.api.errorhandling;

public class ErrorWithStatusDTO {

    private Integer status;
    private String message;

    public ErrorWithStatusDTO(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
