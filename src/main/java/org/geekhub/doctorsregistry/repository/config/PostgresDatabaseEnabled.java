package org.geekhub.doctorsregistry.repository.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PostgresDatabaseEnabled implements Condition {
    @Override
    public boolean matches(
        ConditionContext context,
        @NonNull AnnotatedTypeMetadata metadata
    ) {
        return "pg".equals(context.getEnvironment().getProperty("datasource"));
    }
}