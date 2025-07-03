package com.mate.service;

import com.mate.dto.user.UserRegistrationRequestDto;
import com.mate.dto.user.UserResponseDto;
import com.mate.exception.RegistrationException;
import com.mate.mapper.UserMapper;
import com.mate.model.User;
import com.mate.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register user");
        }
        User user = userMapper.toModel(requestDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}
