package org.geekhub.doctorsregistry.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class UserDetailsManagerConfig {

    private static final String USER_BY_EMAIL_QUERY =
        "select email as username, password, enabled from user_credentials where email = ?";
    private static final String AUTHORITIES_BY_EMAIL_QUERY =
        "select email as username, authority_name from authority where email = ?";

    @Bean
    public JdbcUserDetailsManager userDetailsManager(
        AuthenticationManagerBuilder auth,
        DataSource dataSource,
        PasswordEncoder passwordEncoder
    ) throws Exception {
        return auth.jdbcAuthentication()
            .dataSource(dataSource)
            .passwordEncoder(passwordEncoder)
            .usersByUsernameQuery(USER_BY_EMAIL_QUERY)
            .authoritiesByUsernameQuery(AUTHORITIES_BY_EMAIL_QUERY)
            .getUserDetailsService();
    }

}
