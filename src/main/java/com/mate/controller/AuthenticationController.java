package com.mate.controller;

import com.mate.dto.user.UserRegistrationRequestDto;
import com.mate.dto.user.UserResponseDto;
import com.mate.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("registration")
    public UserResponseDto register(@Valid @RequestBody UserRegistrationRequestDto request) {
        return userService.register(request);
    }

}
