package com.eventbooking.event_booking_system.controller;

import com.eventbooking.event_booking_system.dto.RegisterRequest;
import com.eventbooking.event_booking_system.entity.User;
import com.eventbooking.event_booking_system.dto.UserResponse;
import com.eventbooking.event_booking_system.dto.LoginRequest;
import com.eventbooking.event_booking_system.dto.LoginResponse;
import com.eventbooking.event_booking_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController //tells Spring this class handles HTTP requests and returns response data.
@RequestMapping("/api/users")  //sets the common URL path for this controller.
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse registerUser(@Valid @RequestBody RegisterRequest request) {
        return userService.registerUser(request); //hands registration work to the service
    }

    @PostMapping("/login")
    public LoginResponse loginUser(
            @Valid @RequestBody LoginRequest request) {

        return userService.loginUser(request);
    }

}
