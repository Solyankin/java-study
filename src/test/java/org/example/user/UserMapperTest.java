package org.example.user;

import org.example.controller.user.mapper.UserRequestMapper;
import org.example.controller.user.mapper.UserResponseMapper;
import org.example.controller.user.model.UserRequestDto;
import org.example.controller.user.model.UserResponseDto;
import org.example.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserRequestMapper userRequestMapper;

    @Autowired
    private UserResponseMapper userResponseMapper;

    @Test
    void test_response_dto_to_entity_success_map() {
        User user = new User("FirstName", "SecondName");

        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(user.getExternalId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());

        Assertions.assertEquals(userDto, userResponseMapper.toDto(user));
        Assertions.assertEquals(user, userResponseMapper.toEntity(userDto));
    }

    @Test
    void test_request_dto_to_entity_success_map() {
        User user = new User("FirstName", "SecondName");

        UserRequestDto userDto = new UserRequestDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());

        Assertions.assertEquals(userDto, userRequestMapper.toDto(user));
        Assertions.assertEquals(user, userRequestMapper.toEntity(userDto));
    }
}
