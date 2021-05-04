package org.geekhub.doctorsregistry.web.security.securityconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final int TOKEN_VALIDITY_SECONDS = 7 * 24 * 60 * 60;
    private static final String[] SWAGGER_WHITELIST = {
        "/api/**",
        "/configuration/ui",
        "/swagger-resources/**",
        "/configuration/**",
        "/swagger-ui.html",
        "/webjars/**",
        "/swagger-ui/**",
        "/v3/api-docs"
    };

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .mvcMatchers("/login", "/logout", "/patients/registration").permitAll()
            .mvcMatchers(HttpMethod.POST, "/api/patients").permitAll()

            .mvcMatchers(HttpMethod.POST, "doctor/appointments").hasRole("PATIENT")
            .mvcMatchers("/api/patients/me/appointments/archive",
                "/api/patients/me/appointments/pending", "/patients/me/cabinet").hasRole("PATIENT")
            .mvcMatchers(HttpMethod.POST, "/api/appointments").hasRole("PATIENT")
            .mvcMatchers(HttpMethod.DELETE, "/api/appointments/{appointment-id}").hasRole("PATIENT")

            .mvcMatchers("/doctors/me/cabinet", "/api/doctors/me/appointments/archive",
                "/api/doctors/me/appointments/pending").hasRole("DOCTOR")

            .mvcMatchers("/clinics/me/cabinet").hasRole("CLINIC")
            .mvcMatchers(HttpMethod.POST, "/doctors/registration", "/api/doctors").hasRole("CLINIC")

            .mvcMatchers(HttpMethod.POST, "/clinics", "/api/clinics").hasRole("ADMIN")
            .mvcMatchers("/admins/me/cabinet", "/actuator/**", "/api/users/analytics", "/users/analytics").hasRole("ADMIN")

            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .and()
            .logout().deleteCookies("JSESSIONID")
            .and()
            .rememberMe()
            .key("uniqueAndSecret")
            .tokenValiditySeconds(TOKEN_VALIDITY_SECONDS)
            .rememberMeParameter("remember-me")
            .and()
            .csrf()
            .ignoringAntMatchers(SWAGGER_WHITELIST)
            .ignoringAntMatchers("/actuator/**");
    }
}
