package com.mate.mapper;

import com.mate.config.MapperConfig;
import com.mate.dto.user.UserRegistrationRequestDto;
import com.mate.dto.user.UserResponseDto;
import com.mate.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto userRegistrationRequestDto);
}
