package org.example.service;


import lombok.RequiredArgsConstructor;
import org.example.model.user.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;


    public Optional<User> getByExternalId(String externalId) {
        return repository.findByExternalId(externalId);
    }

    public Optional<User> get(Long id) {
        return repository.findById(id);
    }

    public User create(User user) {
        return repository.save(user);
    }

    public Optional<User> updateByExternalId(String externalId, User data) {
        Optional<User> optionalUser = repository.findByExternalId(externalId);
        optionalUser.ifPresent(user -> update(user, data));
        return optionalUser;
    }

    public Optional<User> update(Long id, User data) {
        Optional<User> optionalUser = get(id);

        optionalUser.ifPresent(user -> update(user, data));
        return optionalUser;
    }

    private void update(User user, User data) {
        user.setFirstName(data.getFirstName());
        user.setLastName(data.getLastName());
        repository.save(user);
    }

    public Optional<User> deleteByExternalId(String externalId) {
        Optional<User> user = repository.findByExternalId(externalId);
        user.ifPresent(repository::delete);
        return user;
    }

    public Optional<User> delete(Long id) {
        Optional<User> user = repository.findById(id);
        user.ifPresent(repository::delete);
        return user;
    }
}

