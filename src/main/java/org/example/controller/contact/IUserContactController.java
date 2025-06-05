package org.example.controller.contact;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.controller.contact.model.UserContactResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "User contacts API", description = "User contacts")
@RequestMapping("/api/v1/contacts")
public interface IUserContactController {

    @Operation(summary = "Get contact by ID")
    @GetMapping("/{id}")
    ResponseEntity<UserContactResponseDto> get(@PathVariable Long id);
}
