package org.example.user;

import org.example.controller.user.UserControllerImpl;
import org.example.controller.user.mapper.UserRequestMapper;
import org.example.controller.user.mapper.UserResponseMapper;
import org.example.controller.user.model.UserResponseDto;
import org.example.model.user.User;
import org.example.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
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

            Assertions.assertThrows(EntityNotFoundException.class, ()->
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

            when(service.deleteByExternalId(expectedUserDto.getId())).thenReturn(Optional.of(expectedUser));
            when(userResponseMapper.toDto(expectedUser)).thenReturn(expectedUserDto);

            ResponseEntity<UserResponseDto> actualUserResponse = controller.delete(expectedUser.getExternalId());

            Assertions.assertEquals(HttpStatus.OK, actualUserResponse.getStatusCode());
            Assertions.assertEquals(expectedUserDto, actualUserResponse.getBody());
        }

        @Test
        void delete_not_found_test() {
            User expectedUser = new User("FirstName", "SecondName");

            Assertions.assertThrows(EntityNotFoundException.class, ()->
                    controller.delete(expectedUser.getExternalId())
            );
        }
    }

    @Nested
    class UpdateTest {
        @Test
        void update_test() {
            User expectedUser = new User("FirstName", "SecondName");

            UserResponseDto expectedUserDto = new UserResponseDto();
            expectedUserDto.setId(expectedUser.getExternalId());
            expectedUserDto.setFirstName(expectedUser.getFirstName());
            expectedUserDto.setLastName(expectedUser.getLastName());

            when(service.updateByExternalId(expectedUser.getExternalId(), expectedUser)).thenReturn(Optional.of(expectedUser));
            when(userResponseMapper.toDto(expectedUser)).thenReturn(expectedUserDto);
            when(userRequestMapper.toEntity(expectedUserDto)).thenReturn(expectedUser);

            ResponseEntity<UserResponseDto> actualUserResponse = controller.update(expectedUser.getExternalId(), expectedUserDto);

            Assertions.assertEquals(expectedUserDto, actualUserResponse.getBody());
        }

        @Test
        void update_not_found_test() {
            User expectedUser = new User("FirstName", "SecondName");

            UserResponseDto expectedUserDto = new UserResponseDto();
            expectedUserDto.setId(expectedUser.getExternalId());
            expectedUserDto.setFirstName(expectedUser.getFirstName());
            expectedUserDto.setLastName(expectedUser.getLastName());

            Assertions.assertThrows(EntityNotFoundException.class, ()->
                    controller.update(expectedUser.getExternalId(), expectedUserDto)
            );
        }
    }
    
}
