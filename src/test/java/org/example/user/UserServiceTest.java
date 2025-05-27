package org.example.user;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;


    @Test
    void get_test() {
        User expectedUser = new User(1L, "FirstName", "SecondName");
        when(repository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));


        Optional<User> actualUser = service.get(expectedUser.getId());
        Assertions.assertTrue(actualUser.isPresent());
        Assertions.assertEquals(expectedUser, actualUser.get());
    }

    @Test
    void get_not_found_test() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertFalse(service.get(1L).isPresent());
    }

    @Test
    void create_test() {
        User expectedUser = new User(1L, "FirstName", "SecondName");
        when(repository.save(expectedUser)).thenReturn(expectedUser);

        User actualUser = service.create(expectedUser);
        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void delete_test() {
        User expectedUser = new User(1L, "FirstName", "SecondName");

        when(repository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));
        doNothing().when(repository).delete(expectedUser);

        Assertions.assertTrue(service.delete(expectedUser.getId()).isPresent());
    }

    @Test
    void delete_not_found_test() {
        User expectedUser = new User(1L, "FirstName", "SecondName");

        when(repository.findById(expectedUser.getId())).thenReturn(Optional.empty());

        Assertions.assertFalse(service.delete(expectedUser.getId()).isPresent());
    }

    @Test
    void update_test() {
        User expectedUser = new User(1L, "FirstName", "SecondName");

        when(repository.save(expectedUser)).thenReturn(expectedUser);
        when(repository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));

        Optional<User> actualUser = service.update(expectedUser.getId(), expectedUser);

        Assertions.assertTrue(actualUser.isPresent());
        Assertions.assertEquals(expectedUser, actualUser.get());
    }

    @Test
    void update_not_found_test() {
        User expectedUser = new User(1L, "FirstName", "SecondName");
        when(repository.findById(expectedUser.getId())).thenReturn(Optional.empty());
        Assertions.assertFalse(service.update(expectedUser.getId(), expectedUser).isPresent());
    }
}
