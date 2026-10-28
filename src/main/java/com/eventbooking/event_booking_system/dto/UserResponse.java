package com.eventbooking.event_booking_system.dto;

import com.eventbooking.event_booking_system.entity.User;

public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private User.Role role;

    public UserResponse(Long id, String fullName, String email, User.Role role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }
    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public User.Role getRole() {
        return role;
    }
}
