package org.example.user;

import org.example.controller.user.mapper.UserRequestMapper;
import org.example.controller.user.mapper.UserResponseMapper;
import org.example.controller.user.model.UserRequestDto;
import org.example.controller.user.model.UserResponseDto;
import org.example.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserRequestMapper userRequestMapper;

    @Autowired
    private UserResponseMapper userResponseMapper;

    @Test
    void test_response_mapper() {
        User user = new User(1L, "FirstName", "SecondName");

        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());

        Assertions.assertEquals(userDto, userResponseMapper.toDto(user));
        Assertions.assertEquals(user, userResponseMapper.toEntity(userDto));
    }

    @Test
    void test_request_mapper() {
        User user = new User(0L, "FirstName", "SecondName");

        UserRequestDto userDto = new UserRequestDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());

        Assertions.assertEquals(userDto, userRequestMapper.toDto(user));
        Assertions.assertEquals(user, userRequestMapper.toEntity(userDto));
    }
}
