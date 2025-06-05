package org.example.controller.user.mapper;

import org.example.controller.user.model.UserRequestDto;
import org.example.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {

    UserRequestDto toDto(User user);

    User toEntity(UserRequestDto userDto);
}
