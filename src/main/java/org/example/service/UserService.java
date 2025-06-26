package org.example.service;


import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.RequiredArgsConstructor;
import org.example.exception.UserNotFoundException;
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
    private final UserObjectMapper mapper;

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
    public User updateByExternalId(String externalId, User patch) throws JsonMappingException {
        User user = repository.findByExternalId(externalId).orElseThrow(
                () -> new UserNotFoundException(externalId)
        );
        update(user, patch);
        return user;
    }

    @CachePut(value = "users", key = "#id")
    public User update(Long id, User patch) throws JsonMappingException {
        User user = get(id).orElseThrow(
                () -> new UserNotFoundException(id)
        );
        update(user, patch);
        return user;
    }

    private void update(User user, User patch) throws JsonMappingException {
        mapper.updateValue(user, patch);
        repository.save(user);
    }

    @CacheEvict(value = "users_external", key = "#externalId")
    public void deleteByExternalId(String externalId) {
        Optional<User> user = repository.findByExternalId(externalId);
        user.ifPresent(repository::delete);
    }

    @CacheEvict(value = "users", key = "#id")
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

