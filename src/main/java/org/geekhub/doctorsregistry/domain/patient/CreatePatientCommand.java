package org.geekhub.doctorsregistry.domain.patient;

import org.springframework.lang.NonNull;

public record CreatePatientCommand(
    @NonNull String email,
    @NonNull String firstName,
    @NonNull String lastName,
    @NonNull String password
) {
}
