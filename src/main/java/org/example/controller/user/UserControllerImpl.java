package org.example.controller.user;

import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.RequiredArgsConstructor;
import org.example.controller.user.mapper.UserRequestMapper;
import org.example.controller.user.mapper.UserResponseMapper;
import org.example.controller.user.model.UserRequestDto;
import org.example.controller.user.model.UserResponseDto;
import org.example.exception.UserNotFoundException;
import org.example.model.user.User;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService service;
    private final UserRequestMapper userRequestMapper;
    private final UserResponseMapper userResponseMapper;

    @Override
    public ResponseEntity<UserResponseDto> get(String id) {
        Optional<User> user = service.getByExternalId(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException(id);
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
    public ResponseEntity<UserResponseDto> update(String id, UserRequestDto userDto) throws JsonMappingException {
        User user = userRequestMapper.toEntity(userDto);
        User updatedUser = service.updateByExternalId(id, user);
        return ResponseEntity.ok(userResponseMapper.toDto(updatedUser));
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        service.deleteByExternalId(id);
        return ResponseEntity.noContent().build();
    }
}