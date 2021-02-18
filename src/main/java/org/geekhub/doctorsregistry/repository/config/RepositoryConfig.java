package org.geekhub.doctorsregistry.repository.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("org.geekhub.doctorsregistry")
@PropertySource("classpath:database.properties")
public class RepositoryConfig {

    @Bean("datasource")
    @Conditional(H2DatabaseEnabled.class)
    HikariDataSource h2DataSource(
        @Value("${datasource.h2.url}") String url,
        @Value("${datasource.h2.username}") String username,
        @Value("${datasource.h2.password}") String password,
        @Value("${datasource.h2.driver}") String driverClassName
    ) {
        return hikariConfig(url, username, password, driverClassName);
    }

    @Bean("datasource")
    @Conditional(PostgresDatabaseEnabled.class)
    HikariDataSource postgresDataSource(
        @Value("${datasource.postgres.url}") String url,
        @Value("${datasource.postgres.username}") String username,
        @Value("${datasource.postgres.password}") String password,
        @Value("${datasource.postgres.driver}") String driverClassName
    ) {
        return hikariConfig(url, username, password, driverClassName);
    }

    public HikariDataSource hikariConfig(
        String url,
        String username,
        String password,
        String driverClassName
    ) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setPoolName("DoctorsRegistryApp");
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setConnectionTimeout(1000);
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public MigrateResult migrateDatabase(
        @Qualifier("datasource") HikariDataSource dataSource,
        @Value("classpath:db/migration") String location
    ) {
        return Flyway.configure()
            .dataSource(dataSource)
            .locations(location)
            .load()
            .migrate();
    }
}
