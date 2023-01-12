package com.example.homeservicespringmvc.security.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfigProvider {


    private final CustomerDetailServiceImpl customerDetailService;
    private final SpecialistDetailServiceImpl specialistDetailService;
    private final ManagerDetailServiceImpl managerDetailService;

    private final PasswordEncoder passwordEncoder;





    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/specialist/signup").permitAll()
                .requestMatchers("/api/v1/customer/signup").permitAll()
                .requestMatchers("/api/v1/manager/signup").permitAll()
                .requestMatchers("/api/v1/customer/**").authenticated()
                .requestMatchers("/api/v1/manager/**").authenticated()
                .requestMatchers("/api/v1/specialist/**").authenticated()
                .requestMatchers("/api/v1/**").authenticated()
                .and()
                .httpBasic();

        return http.build();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerDetailService).passwordEncoder(passwordEncoder);
        auth.userDetailsService(specialistDetailService).passwordEncoder(passwordEncoder);
        auth.userDetailsService(managerDetailService).passwordEncoder(passwordEncoder);


    }


}
