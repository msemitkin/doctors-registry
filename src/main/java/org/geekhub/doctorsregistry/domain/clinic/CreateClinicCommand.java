package org.geekhub.doctorsregistry.domain.clinic;

import org.springframework.lang.NonNull;

public record CreateClinicCommand(
    @NonNull String email,
    @NonNull String name,
    @NonNull String address,
    @NonNull String password
) {
}
