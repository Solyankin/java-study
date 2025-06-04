package org.example.controller.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.controller.user.model.UserRequestDto;
import org.example.controller.user.model.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User API", description = "Users")
interface IUserController {
    @GetMapping("/{id}")
    ResponseEntity<UserResponseDto> get(@PathVariable Long id);

    @PostMapping
    ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto userDto);

    @PutMapping("/{id}")
    ResponseEntity<UserResponseDto> update(@PathVariable Long id, @RequestBody UserRequestDto userDto);

    @DeleteMapping("/{id}")
    ResponseEntity<UserResponseDto> delete(@PathVariable Long id);
}
