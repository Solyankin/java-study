package org.example.service;


import lombok.RequiredArgsConstructor;
import org.example.model.user.User;
import org.example.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    @Cacheable(value = "users_external", key = "#externalId")
    public Optional<User> getByExternalId(String externalId) {
        return repository.findByExternalId(externalId);
    }

    @Cacheable(value = "users", key = "#id")
    public Optional<User> get(Long id) {
        return repository.findById(id);
    }


    public User create(User user) {
        return repository.save(user);
    }

    @CachePut(value = "users_external", key = "#externalId")
    public Optional<User> updateByExternalId(String externalId, User patch) {
        Optional<User> optionalUser = repository.findByExternalId(externalId);
        optionalUser.ifPresent(user -> update(user, patch));
        return optionalUser;
    }

    @CachePut(value = "users", key = "#id")
    public Optional<User> update(Long id, User patch) {
        Optional<User> optionalUser = get(id);

        optionalUser.ifPresent(user -> update(user, patch));
        return optionalUser;
    }

    private void update(User user, User patch) {
        user.setFirstName(patch.getFirstName());
        user.setLastName(patch.getLastName());
        repository.save(user);
    }

    @CacheEvict(value = "users_external", key = "#externalId")
    public Optional<User> deleteByExternalId(String externalId) {
        Optional<User> user = repository.findByExternalId(externalId);
        user.ifPresent(repository::delete);
        return user;
    }

    @CacheEvict(value = "users", key = "#id")
    public Optional<User> delete(Long id) {
        Optional<User> user = repository.findById(id);
        user.ifPresent(repository::delete);
        return user;
    }
}

