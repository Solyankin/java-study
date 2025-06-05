package org.example.controller.user.mapper;

import org.example.controller.user.model.UserResponseDto;
import org.example.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    @Mapping(source = "externalId", target = "id")
    UserResponseDto toDto(User user);

    @Mapping(source = "id", target = "externalId")
    @Mapping(target = "id", ignore = true)
    User toEntity(UserResponseDto userDto);
}
