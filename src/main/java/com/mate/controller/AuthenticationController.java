package com.mate.controller;

import com.mate.dto.user.UserLoginRequestDto;
import com.mate.dto.user.UserLoginResponseDto;
import com.mate.dto.user.UserRegistrationRequestDto;
import com.mate.dto.user.UserResponseDto;
import com.mate.security.AuthenticationService;
import com.mate.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Endpoints for registration and login")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    public UserResponseDto register(@Valid @RequestBody UserRegistrationRequestDto request) {
        System.out.println("EMAIL: " + request.getEmail());
        System.out.println("FIRST NAME: " + request.getFirstName());
        System.out.println("LAST NAME: " + request.getLastName());
        System.out.println("ADDRESS: " + request.getShippingAddress());
        return userService.register(request);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
