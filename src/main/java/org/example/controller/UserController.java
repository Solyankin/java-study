package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Long id) {
        Optional<User> user = service.get(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.ok(service.create(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        Optional<User> updatedUser = service.update(id, user);
        return updatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> delete(@PathVariable Long id) {
        Optional<User> user = service.delete(id);

        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

}