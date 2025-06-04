package org.example.user;

import org.example.controller.user.UserController;
import org.example.controller.user.mapper.UserRequestMapper;
import org.example.controller.user.mapper.UserResponseMapper;
import org.example.controller.user.model.UserResponseDto;
import org.example.model.user.User;
import org.example.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;
import java.util.stream.Stream;

import static org.example.utils.StringUtils.generateRandomString;
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

    @Nested
    class GetTest {
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
    }

    @Nested
    class CreateTest {
        @ParameterizedTest
        @MethodSource("org.example.user.UserControllerTest#validUser")
        void create_test(String firstName, String secondName) {
            User expectedUser = new User(1L, firstName, secondName);

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

        @ParameterizedTest
        @MethodSource("org.example.user.UserControllerTest#invalidUser")
        void create_invalid_test(String firstName, String secondName) {
            User expectedUser = new User(1L, firstName, secondName);

            UserResponseDto expectedUserDto = new UserResponseDto();
            expectedUserDto.setId(expectedUser.getId());
            expectedUserDto.setFirstName(expectedUser.getFirstName());
            expectedUserDto.setLastName(expectedUser.getLastName());

            when(service.create(expectedUser)).thenReturn(expectedUser);
            when(userResponseMapper.toDto(expectedUser)).thenReturn(expectedUserDto);
            when(userRequestMapper.toEntity(expectedUserDto)).thenReturn(expectedUser);

            ResponseEntity<UserResponseDto> actualResponse =  controller.create(expectedUserDto);

            Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());
        }
    }

    @Nested
    class DeleteTest {
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
    }

    @Nested
    class UpdateTest {
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


    static Stream<Arguments> validUser() {
        return Stream.of(
                Arguments.of(generateRandomString(2), generateRandomString(50)),
                Arguments.of(generateRandomString(50), generateRandomString(2))
        );
    }

    static Stream<Arguments> invalidUser() {
        return Stream.of(
                Arguments.of(generateRandomString(null), generateRandomString(25)),
                Arguments.of(generateRandomString(0), generateRandomString(25)),
                Arguments.of(generateRandomString(1), generateRandomString(25)),
                Arguments.of(generateRandomString(51), generateRandomString(25)),
                Arguments.of(generateRandomString(25), generateRandomString(null)),
                Arguments.of(generateRandomString(25), generateRandomString(0)),
                Arguments.of(generateRandomString(25), generateRandomString(1)),
                Arguments.of(generateRandomString(25), generateRandomString(51))
        );
    }
}
