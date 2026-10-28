package com.eventbooking.event_booking_system.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    @Bean //tells spring to create and manage the return object
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
