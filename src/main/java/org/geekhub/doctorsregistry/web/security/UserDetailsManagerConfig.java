package org.geekhub.doctorsregistry.web.security;

import org.geekhub.doctorsregistry.repository.util.SQLManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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

    private final SQLManager sqlManager;

    public UserDetailsManagerConfig(SQLManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    @Bean
    public JdbcUserDetailsManager userDetailsManager(
        AuthenticationManagerBuilder auth,
        DataSource dataSource,
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager
    ) throws Exception {
        JdbcUserDetailsManager userDetailsManager = auth.jdbcAuthentication()
            .dataSource(dataSource)
            .passwordEncoder(passwordEncoder)
            .usersByUsernameQuery(USER_BY_EMAIL_QUERY)
            .authoritiesByUsernameQuery(AUTHORITIES_BY_EMAIL_QUERY)
            .getUserDetailsService();

        userDetailsManager.setAuthenticationManager(authenticationManager);
        userDetailsManager.setUserExistsSql(sqlManager.getQuery("user-exists"));
        userDetailsManager.setCreateUserSql(sqlManager.getQuery("create-user"));
        userDetailsManager.setCreateAuthoritySql(sqlManager.getQuery("create-authority"));
        userDetailsManager.setDeleteUserSql(sqlManager.getQuery("delete-user"));
        userDetailsManager.setDeleteUserAuthoritiesSql(sqlManager.getQuery("delete-authority"));
        userDetailsManager.setChangePasswordSql(sqlManager.getQuery("change-password"));

        return userDetailsManager;
    }

}
