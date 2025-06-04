package org.example.controller.user.mapper;

import org.example.controller.user.model.UserResponseDto;
import org.example.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    UserResponseDto toDto(User user);

    User toEntity(UserResponseDto userDto);
}
