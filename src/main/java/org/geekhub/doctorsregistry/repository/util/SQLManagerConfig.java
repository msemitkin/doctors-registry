package org.geekhub.doctorsregistry.repository.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:common.properties")
public class SQLManagerConfig {

    @Bean
    public SQLManager sqlManager(@Value("${sql-resource-path-pattern}") String pattern) {
        return new SQLManager(pattern);
    }

}
