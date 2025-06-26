package org.example.user;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.example.exception.UserNotFoundException;
import org.example.model.user.User;
import org.example.repository.UserRepository;
import org.example.service.UserObjectMapper;
import org.example.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserObjectMapper mapper;

    @InjectMocks
    private UserService service;


    @Test
    void get_test() {
        User expectedUser = new User("FirstName", "SecondName");
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
        User expectedUser = new User("FirstName", "SecondName");
        when(repository.save(expectedUser)).thenReturn(expectedUser);

        User actualUser = service.create(expectedUser);
        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void delete_test() throws IOException {
        User expectedUser = new User("FirstName", "SecondName");

        when(repository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));
        doNothing().when(repository).delete(expectedUser);
        when(mapper.updateValue(any(), any())).thenReturn(expectedUser);

        service.delete(expectedUser.getId());
        Assertions.assertTrue(service.get(expectedUser.getId()).isEmpty());
    }

    @Test
    void delete_not_found_test() {
        User expectedUser = new User("FirstName", "SecondName");

        when(repository.findById(expectedUser.getId())).thenReturn(Optional.empty());
        service.delete(expectedUser.getId());
        Assertions.assertTrue(service.get(expectedUser.getId()).isEmpty());
    }

    @Test
    void update_test() throws IOException {
        User expectedUser = new User("FirstName", "SecondName");

        when(repository.save(expectedUser)).thenReturn(expectedUser);
        when(repository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));
        when(mapper.updateValue(any(), any())).thenReturn(expectedUser);

        User actualUser = service.update(expectedUser.getId(), expectedUser);
        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void update_not_found_test() {
        User expectedUser = new User("FirstName", "SecondName");
        when(repository.findById(expectedUser.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class,
                () -> service.update(expectedUser.getId(), expectedUser)
        );
    }
}
