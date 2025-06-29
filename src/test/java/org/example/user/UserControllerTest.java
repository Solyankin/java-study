package org.example.user;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.example.controller.user.UserControllerImpl;
import org.example.controller.user.mapper.UserRequestMapper;
import org.example.controller.user.mapper.UserResponseMapper;
import org.example.controller.user.model.UserResponseDto;
import org.example.exception.UserNotFoundException;
import org.example.model.user.User;
import org.example.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
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
    private UserControllerImpl controller;

    @Nested
    class GetTest {
        @Test
        void get_test() {
            User expectedUser = new User("FirstName", "SecondName");

            UserResponseDto expectedUserDto = new UserResponseDto();
            expectedUserDto.setId(expectedUser.getExternalId());
            expectedUserDto.setFirstName(expectedUser.getFirstName());
            expectedUserDto.setLastName(expectedUser.getLastName());

            when(service.getByExternalId(expectedUser.getExternalId())).thenReturn(Optional.of(expectedUser));
            when(userResponseMapper.toDto(expectedUser)).thenReturn(expectedUserDto);

            UserResponseDto actualUserDto = controller.get(expectedUser.getExternalId()).getBody();
            Assertions.assertEquals(expectedUserDto, actualUserDto);
        }

        @Test
        void get_not_found_test() {
            when(service.getByExternalId(anyString())).thenReturn(Optional.empty());

            Assertions.assertThrows(EntityNotFoundException.class, () ->
                    controller.get(UUID.randomUUID().toString())
            );
        }
    }

    @Nested
    class CreateTest {

        @Test
        void create_test() {
            User expectedUser = new User("FirstName", "SecondName");

            UserResponseDto expectedUserDto = new UserResponseDto();
            expectedUserDto.setId(expectedUser.getExternalId());
            expectedUserDto.setFirstName(expectedUser.getFirstName());
            expectedUserDto.setLastName(expectedUser.getLastName());

            when(service.create(expectedUser)).thenReturn(expectedUser);
            when(userResponseMapper.toDto(expectedUser)).thenReturn(expectedUserDto);
            when(userRequestMapper.toEntity(expectedUserDto)).thenReturn(expectedUser);

            UserResponseDto actualUserDto = controller.create(expectedUserDto).getBody();

            Assertions.assertEquals(expectedUserDto, actualUserDto);
        }
    }

    @Nested
    class DeleteTest {
        @Test
        void delete_test() {
            User expectedUser = new User("FirstName", "SecondName");

            UserResponseDto expectedUserDto = new UserResponseDto();
            expectedUserDto.setId(expectedUser.getExternalId());
            expectedUserDto.setFirstName(expectedUser.getFirstName());
            expectedUserDto.setLastName(expectedUser.getLastName());

            ResponseEntity<Void> actualUserResponse = controller.delete(expectedUser.getExternalId());
            Assertions.assertEquals(HttpStatus.NO_CONTENT, actualUserResponse.getStatusCode());
        }
    }

    @Nested
    class UpdateTest {
        @Test
        void update_test() throws IOException {
            User expectedUser = new User("FirstName", "SecondName");

            UserResponseDto expectedUserDto = new UserResponseDto();
            expectedUserDto.setId(expectedUser.getExternalId());
            expectedUserDto.setFirstName(expectedUser.getFirstName());
            expectedUserDto.setLastName(expectedUser.getLastName());

            when(service.updateByExternalId(expectedUser.getExternalId(), expectedUser)).thenReturn(expectedUser);
            when(userResponseMapper.toDto(expectedUser)).thenReturn(expectedUserDto);
            when(userRequestMapper.toEntity(expectedUserDto)).thenReturn(expectedUser);

            ResponseEntity<UserResponseDto> actualUserResponse = controller.update(expectedUser.getExternalId(), expectedUserDto);

            Assertions.assertEquals(expectedUserDto, actualUserResponse.getBody());
        }

        @Test
        void update_not_found_test() throws IOException {
            User expectedUser = new User("FirstName", "SecondName");

            UserResponseDto expectedUserDto = new UserResponseDto();
            expectedUserDto.setId(expectedUser.getExternalId());
            expectedUserDto.setFirstName(expectedUser.getFirstName());
            expectedUserDto.setLastName(expectedUser.getLastName());
            when(service.getByExternalId(expectedUser.getExternalId())).thenReturn(Optional.empty());

            var u = controller.update(expectedUser.getExternalId(), expectedUserDto);
            Assertions.assertThrows(UserNotFoundException.class, () ->
                    controller.update(expectedUser.getExternalId(), expectedUserDto)
            );
        }
    }

}
