package com.eventbooking.event_booking_system.dto;

public class LoginResponse {

    private final UserResponse user;
    private final String token;

    public LoginResponse(UserResponse user, String token) {
        this.user = user;
        this.token = token;
    }

    public UserResponse getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}