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
            .mvcMatchers(HttpMethod.POST, "doctor/appointments").hasRole("PATIENT")
            .mvcMatchers("/patients/me/cabinet").hasRole("PATIENT")

            .mvcMatchers("/doctors/me/cabinet").hasRole("DOCTOR")

            .mvcMatchers("/clinics/me/cabinet").hasRole("CLINIC")
            .mvcMatchers(HttpMethod.POST, "/doctors/registration").hasRole("CLINIC")

            .mvcMatchers(HttpMethod.POST, "clinics/").hasRole("ADMIN")
            .mvcMatchers("/admins/me/cabinet").hasRole("ADMIN")

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
