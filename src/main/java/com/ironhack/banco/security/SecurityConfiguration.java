package com.ironhack.banco.security;

import com.ironhack.banco.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }

   @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.httpBasic();
        http.csrf().disable();
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/accounts/**").hasAnyRole("ADMIN", "ACCOUNT_HOLDER")
                .mvcMatchers(HttpMethod.PATCH, "/accounts/**").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.GET, "/all").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.POST, "/thirdparty").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.POST, "/accounts/create/**").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.POST, "/accounts/**").hasAnyRole("ACCOUNT_HOLDER")
                .anyRequest().permitAll();
    }

}
