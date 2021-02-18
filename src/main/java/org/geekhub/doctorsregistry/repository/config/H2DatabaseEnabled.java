package org.geekhub.doctorsregistry.repository.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class H2DatabaseEnabled implements Condition {
    @Override
    public boolean matches(
        ConditionContext context,
        @NonNull AnnotatedTypeMetadata metadata
    ) {
        return "h2".equals(context.getEnvironment().getProperty("datasource"));
    }
}