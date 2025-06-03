package org.example.user;

import org.example.controller.UserController;
import org.example.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class UserControllerTest {

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;


    @Test
    void get_test() {
        User expectedUser = new User(1L, "FirstName", "SecondName");

        when(service.get(expectedUser.getId())).thenReturn(Optional.of(expectedUser));

        User actualUser = controller.get(expectedUser.getId()).getBody();
        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void get_not_found_test() {
        when(service.get(1L)).thenReturn(Optional.empty());

        ResponseEntity<User> actualUser = controller.get(1L);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actualUser.getStatusCode());
        Assertions.assertNull(actualUser.getBody());
    }

    @Test
    void create_test() {
        User expectedUser = new User(1L, "FirstName", "SecondName");
        when(service.create(expectedUser)).thenReturn(expectedUser);

        User actualUser = controller.create(expectedUser).getBody();

        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void delete_test() {
        User expectedUser = new User(1L, "FirstName", "SecondName");

        when(service.delete(expectedUser.getId())).thenReturn(Optional.of(expectedUser));
        ResponseEntity<User> actualUser = controller.delete(expectedUser.getId());

        Assertions.assertEquals(HttpStatus.OK, actualUser.getStatusCode());
        Assertions.assertEquals(expectedUser, actualUser.getBody());
    }

    @Test
    void delete_not_found_test() {
        User expectedUser = new User(1L, "FirstName", "SecondName");

        when(service.get(expectedUser.getId())).thenReturn(Optional.empty());

        ResponseEntity<User> actualUser = controller.delete(expectedUser.getId());

        Assertions.assertEquals(HttpStatus.NO_CONTENT, actualUser.getStatusCode());
        Assertions.assertNull(actualUser.getBody());
    }

    @Test
    void update_test() {
        User expectedUser = new User(1L, "FirstName", "SecondName");

        when(service.update(expectedUser.getId(), expectedUser)).thenReturn(Optional.of(expectedUser));

        ResponseEntity<User> actualUser = controller.update(expectedUser.getId(), expectedUser);

        Assertions.assertEquals(expectedUser, actualUser.getBody());
    }

    @Test
    void update_not_found_test() {
        User expectedUser = new User(1L, "FirstName", "SecondName");

        when(service.get(expectedUser.getId())).thenReturn(Optional.empty());

        ResponseEntity<User> actualUser = controller.update(expectedUser.getId(), expectedUser);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, actualUser.getStatusCode());
        Assertions.assertNull(actualUser.getBody());
    }
}
