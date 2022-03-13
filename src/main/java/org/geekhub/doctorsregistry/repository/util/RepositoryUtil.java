package org.geekhub.doctorsregistry.repository.util;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.lang.NonNull;

import java.util.function.Supplier;

public class RepositoryUtil {
    private RepositoryUtil() {
    }

    public static <R> R wrapWithEmptyResultDataAccessException(
        @NonNull Supplier<R> callable,
        @NonNull Supplier<R> defaultValue
    ) {
        try {
            return callable.get();
        } catch (EmptyResultDataAccessException e) {
            return defaultValue.get();
        }
    }
}
