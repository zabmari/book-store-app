package com.mate.service;

import com.mate.dto.user.UserRegistrationRequestDto;
import com.mate.dto.user.UserResponseDto;

public interface UserService {

    UserResponseDto register(UserRegistrationRequestDto requestDto);
}
