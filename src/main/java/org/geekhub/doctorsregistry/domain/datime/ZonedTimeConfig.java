package org.geekhub.doctorsregistry.domain.datime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:common.properties")
public class ZonedTimeConfig {

    @Bean
    public ZonedTime zonedTime(@Value("${timezone}") String timeZone) {
        return new ZonedTime(timeZone);
    }

}
