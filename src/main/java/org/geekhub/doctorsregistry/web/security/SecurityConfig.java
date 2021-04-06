package org.geekhub.doctorsregistry.web.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .mvcMatchers(HttpMethod.POST, "doctor/appointments").hasRole("PATIENT")
            .mvcMatchers("/doctors/me/cabinet").hasRole("DOCTOR")
            .mvcMatchers("/patients/me/cabinet").hasRole("PATIENT")
            .mvcMatchers("/login", "/logout").permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
            .and()
            .logout()
            .and()
            .csrf().ignoringAntMatchers(SWAGGER_WHITELIST);
    }
}
