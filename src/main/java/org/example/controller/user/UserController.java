package org.example.controller.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.controller.user.mapper.UserRequestMapper;
import org.example.controller.user.mapper.UserResponseMapper;
import org.example.controller.user.model.UserRequestDto;
import org.example.controller.user.model.UserResponseDto;
import org.example.model.user.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User API", description = "Users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final UserRequestMapper userRequestMapper;
    private final UserResponseMapper userResponseMapper;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> get(@PathVariable Long id) {
        Optional<User> user = service.get(id);

        return user
                .map(u -> ResponseEntity.ok(userResponseMapper.toDto(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto userDto) {
        User user = userRequestMapper.toEntity(userDto);
        User createdUser = service.create(user);
        return ResponseEntity.ok(userResponseMapper.toDto(createdUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @Valid @RequestBody UserRequestDto userDto) {
        User user = userRequestMapper.toEntity(userDto);
        Optional<User> updatedUser = service.update(id, user);

        return updatedUser
                .map(u -> ResponseEntity.ok(userResponseMapper.toDto(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDto> delete(@PathVariable Long id) {
        Optional<User> user = service.delete(id);

        return user
                .map(u -> ResponseEntity.ok(userResponseMapper.toDto(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}