package org.geekhub.doctorsregistry.repository.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ComponentScan("org.geekhub.doctorsregistry")
public class RepositoryConfig {

    @Bean("datasource")
    @Profile("dev")
    HikariDataSource h2DataSource(
        @Value("${spring.datasource.url}") String url,
        @Value("${spring.datasource.username}") String username,
        @Value("${spring.datasource.password}") String password,
        @Value("${spring.datasource.driver-class-name}") String driverClassName
    ) {
        return hikariConfig(url, username, password, driverClassName);
    }

    @Bean("datasource")
    @Profile("prod")
    HikariDataSource postgresDataSource(
        @Value("${spring.datasource.url}") String url,
        @Value("${spring.datasource.username}") String username,
        @Value("${spring.datasource.password}") String password,
        @Value("${spring.datasource.driver-class-name}") String driverClassName
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
