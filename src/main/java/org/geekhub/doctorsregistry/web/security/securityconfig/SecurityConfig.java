package org.geekhub.doctorsregistry.web.security.securityconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.time.Duration;

import static org.geekhub.doctorsregistry.web.security.role.Role.ADMIN;
import static org.geekhub.doctorsregistry.web.security.role.Role.CLINIC;
import static org.geekhub.doctorsregistry.web.security.role.Role.PATIENT;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final int TOKEN_VALIDITY_SECONDS = (int) Duration.ofDays(7).toSeconds();
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
            .mvcMatchers("/login", "/logout").permitAll()
            .mvcMatchers("/patients/registration").hasRole("ANONYMOUS")
            .mvcMatchers(HttpMethod.POST, "/api/patients").hasRole("ANONYMOUS")

            .mvcMatchers(HttpMethod.POST, "doctor/appointments").hasRole(PATIENT.name())
            .mvcMatchers("/api/patients/me/appointments/archive",
                "/api/patients/me/appointments/pending", "/patients/me/cabinet").hasRole(PATIENT.name())
            .mvcMatchers(HttpMethod.POST, "/api/appointments").hasRole(PATIENT.name())
            .mvcMatchers(HttpMethod.DELETE, "/api/appointments/{appointment-id}").hasRole(PATIENT.name())

            .mvcMatchers("/doctors/me/cabinet", "/api/doctors/me/appointments/archive",
                "/api/doctors/me/appointments/pending").hasRole("DOCTOR")

            .mvcMatchers("/clinics/me/cabinet").hasRole(CLINIC.name())
            .mvcMatchers(HttpMethod.POST, "/doctors/registration", "/api/doctors").hasRole(CLINIC.name())

            .mvcMatchers(HttpMethod.POST, "/clinics", "/api/clinics").hasRole(ADMIN.name())
            .mvcMatchers(HttpMethod.GET, "/api/patients/*").hasRole(ADMIN.name())
            .mvcMatchers("/admins/me/cabinet", "/actuator/**", "/api/users/analytics", "/analytics").hasRole(ADMIN.name())

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
