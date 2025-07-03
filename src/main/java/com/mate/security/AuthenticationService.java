package com.mate.security;

import com.mate.dto.user.UserLoginRequestDto;
import com.mate.dto.user.UserLoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.email(), requestDto.password())
        );

        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }
}
