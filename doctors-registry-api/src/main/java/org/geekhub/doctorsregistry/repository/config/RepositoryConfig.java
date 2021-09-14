package org.geekhub.doctorsregistry.repository.config;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.geekhub.doctorsregistry")
public class RepositoryConfig {

    @Bean
    public MigrateResult migrateDatabase(
        HikariDataSource dataSource,
        @Value("classpath:db/migration") String location
    ) {
        return Flyway.configure()
            .dataSource(dataSource)
            .locations(location)
            .load()
            .migrate();
    }
}
