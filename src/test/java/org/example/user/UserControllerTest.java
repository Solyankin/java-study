package org.example.user;

import org.example.controller.user.UserController;
import org.example.controller.user.mapper.UserRequestMapper;
import org.example.controller.user.mapper.UserResponseMapper;
import org.example.controller.user.model.UserResponseDto;
import org.example.model.user.User;
import org.example.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService service;

    @Mock
    private UserRequestMapper userRequestMapper;

    @Mock
    private UserResponseMapper userResponseMapper;

    @InjectMocks
    private UserController controller;


    @Test
    void get_test() {
        User expectedUser = new User(1L, "FirstName", "SecondName");

        UserResponseDto expectedUserDto = new UserResponseDto();
        expectedUserDto.setId(expectedUser.getId());
        expectedUserDto.setFirstName(expectedUser.getFirstName());
        expectedUserDto.setLastName(expectedUser.getLastName());

        when(service.get(expectedUser.getId())).thenReturn(Optional.of(expectedUser));
        when(userResponseMapper.toDto(expectedUser)).thenReturn(expectedUserDto);

        UserResponseDto actualUserDto = controller.get(expectedUser.getId()).getBody();
        Assertions.assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    void get_not_found_test() {
        when(service.get(1L)).thenReturn(Optional.empty());

        ResponseEntity<UserResponseDto> actualUser = controller.get(1L);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actualUser.getStatusCode());
        Assertions.assertNull(actualUser.getBody());
    }

    @Test
    void create_test() {
        User expectedUser = new User(1L, "FirstName", "SecondName");

        UserResponseDto expectedUserDto = new UserResponseDto();
        expectedUserDto.setId(expectedUser.getId());
        expectedUserDto.setFirstName(expectedUser.getFirstName());
        expectedUserDto.setLastName(expectedUser.getLastName());

        when(service.create(expectedUser)).thenReturn(expectedUser);
        when(userResponseMapper.toDto(expectedUser)).thenReturn(expectedUserDto);
        when(userRequestMapper.toEntity(expectedUserDto)).thenReturn(expectedUser);

        UserResponseDto actualUserDto = controller.create(expectedUserDto).getBody();

        Assertions.assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    void delete_test() {
        User expectedUser = new User(1L, "FirstName", "SecondName");

        UserResponseDto expectedUserDto = new UserResponseDto();
        expectedUserDto.setId(expectedUser.getId());
        expectedUserDto.setFirstName(expectedUser.getFirstName());
        expectedUserDto.setLastName(expectedUser.getLastName());

        when(service.delete(expectedUserDto.getId())).thenReturn(Optional.of(expectedUser));
        when(userResponseMapper.toDto(expectedUser)).thenReturn(expectedUserDto);

        ResponseEntity<UserResponseDto> actualUserResponse = controller.delete(expectedUser.getId());

        Assertions.assertEquals(HttpStatus.OK, actualUserResponse.getStatusCode());
        Assertions.assertEquals(expectedUserDto, actualUserResponse.getBody());
    }

    @Test
    void delete_not_found_test() {
        User expectedUser = new User(1L, "FirstName", "SecondName");

        ResponseEntity<UserResponseDto> actualUserResponse = controller.delete(expectedUser.getId());

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actualUserResponse.getStatusCode());
        Assertions.assertNull(actualUserResponse.getBody());
    }

    @Test
    void update_test() {
        User expectedUser = new User(1L, "FirstName", "SecondName");

        UserResponseDto expectedUserDto = new UserResponseDto();
        expectedUserDto.setId(expectedUser.getId());
        expectedUserDto.setFirstName(expectedUser.getFirstName());
        expectedUserDto.setLastName(expectedUser.getLastName());

        when(service.update(expectedUser.getId(), expectedUser)).thenReturn(Optional.of(expectedUser));
        when(userResponseMapper.toDto(expectedUser)).thenReturn(expectedUserDto);
        when(userRequestMapper.toEntity(expectedUserDto)).thenReturn(expectedUser);

        ResponseEntity<UserResponseDto> actualUserResponse = controller.update(expectedUser.getId(), expectedUserDto);

        Assertions.assertEquals(expectedUserDto, actualUserResponse.getBody());
    }

    @Test
    void update_not_found_test() {
        User expectedUser = new User(1L, "FirstName", "SecondName");

        UserResponseDto expectedUserDto = new UserResponseDto();
        expectedUserDto.setId(expectedUser.getId());
        expectedUserDto.setFirstName(expectedUser.getFirstName());
        expectedUserDto.setLastName(expectedUser.getLastName());

        ResponseEntity<UserResponseDto> actualUserDto = controller.update(expectedUser.getId(), expectedUserDto);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, actualUserDto.getStatusCode());
        Assertions.assertNull(actualUserDto.getBody());
    }
}
