package org.example.controller.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.controller.user.mapper.UserRequestMapper;
import org.example.controller.user.mapper.UserResponseMapper;
import org.example.controller.user.model.UserRequestDto;
import org.example.controller.user.model.UserResponseDto;
import org.example.model.user.User;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

//@RestController
//@RequestMapping("/api/v1/users")
//@Tag(name = "User API", description = "Users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService service;
    private final UserRequestMapper userRequestMapper;
    private final UserResponseMapper userResponseMapper;

    @Override
    public ResponseEntity<UserResponseDto> get(String id) {
        Optional<User> user = service.getByExternalId(id);

        if (user.isEmpty()) {
            throwExceptionIfUserNotFound(id);
        }

        return ResponseEntity.ok(userResponseMapper.toDto(user.get()));

    }

    @Override
    public ResponseEntity<UserResponseDto> create(UserRequestDto userDto) {
        User user = userRequestMapper.toEntity(userDto);
        User createdUser = service.create(user);
        return ResponseEntity.ok(userResponseMapper.toDto(createdUser));
    }

    @Override
    public ResponseEntity<UserResponseDto> update(String id, UserRequestDto userDto) {
        User user = userRequestMapper.toEntity(userDto);
        Optional<User> updatedUser = service.updateByExternalId(id, user);

        if (updatedUser.isEmpty()) {
            throwExceptionIfUserNotFound(id);
        }

        return ResponseEntity.ok(userResponseMapper.toDto(user));
    }

    @Override
    public ResponseEntity<UserResponseDto> delete(String id) {
        Optional<User> user = service.deleteByExternalId(id);

        if (user.isEmpty()) {
            throwExceptionIfUserNotFound(id);
        }

        return ResponseEntity.ok(userResponseMapper.toDto(user.get()));
    }

    private void throwExceptionIfUserNotFound(String id) {
        throw new EntityNotFoundException(String.format("User not found with id '%s'", id));
    }
}