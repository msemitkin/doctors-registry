package org.geekhub.doctorsregistry.web.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .mvcMatchers(HttpMethod.POST, "doctor/appointments").hasRole("PATIENT")
            .mvcMatchers("/login", "/logout").permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
            .and()
            .logout();
    }
}
