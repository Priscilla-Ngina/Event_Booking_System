package com.eventbooking.event_booking_system.service;

import com.eventbooking.event_booking_system.dto.RegisterRequest;
import com.eventbooking.event_booking_system.entity.User;
import com.eventbooking.event_booking_system.repository.UserRepository;
import com.eventbooking.event_booking_system.dto.LoginRequest;
import com.eventbooking.event_booking_system.exception.InvalidCredentialsException;
import com.eventbooking.event_booking_system.security.JwtService;
import com.eventbooking.event_booking_system.dto.LoginResponse;
import com.eventbooking.event_booking_system.dto.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;



@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService= jwtService;
    }

    public UserResponse registerUser(RegisterRequest request){

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        user.setRole(User.Role.CUSTOMER);

        User savedUser = userRepository.save(user);
        return new UserResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    public LoginResponse loginUser(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        );

        if (!passwordMatches) {
            throw new InvalidCredentialsException();
        }

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole()
        );

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(userResponse, token);
    }

}
