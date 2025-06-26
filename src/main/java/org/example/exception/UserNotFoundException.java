package org.example.exception;

import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(String keyOrId) {
        super("User not found with key or id = " + keyOrId);
    }

    public UserNotFoundException(Long id) {
        this(id.toString());
    }
}
