package org.geekhub.doctorsregistry.web.api.errorhandling;

import java.util.Map;

public class ErrorMapDTO {
    private final Map<String, ErrorDTO> errors;

    public ErrorMapDTO(Map<String, ErrorDTO> errors) {
        this.errors = errors;
    }

    public Map<String, ErrorDTO> getErrors() {
        return errors;
    }
}