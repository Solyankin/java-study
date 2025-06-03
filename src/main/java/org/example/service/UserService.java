package org.example.service;


import lombok.RequiredArgsConstructor;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;


    public Optional<User> get(Long id) {
        return repository.findById(id);
    }

    public User create(User user) {
        return repository.save(user);
    }

    public Optional<User> update(Long id, User u) {
        Optional<User> optionalUser = get(id);

        optionalUser.ifPresent(user -> {
            user.setFirstName(u.getFirstName());
            user.setSecondName(u.getSecondName());
            repository.save(user);
        });

        return optionalUser;

    }

    public Optional<User> delete(Long id) {
        Optional<User> user = repository.findById(id);
        user.ifPresent(repository::delete);
        return user;
    }
}

