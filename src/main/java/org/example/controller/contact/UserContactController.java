package org.example.controller.contact;

import org.example.controller.contact.model.UserContactResponseDto;
import org.springframework.http.ResponseEntity;

public class UserContactController implements IUserContactController {

    @Override
    public ResponseEntity<UserContactResponseDto> get(Long userID) {
        throw new UnsupportedOperationException();
    }
}
