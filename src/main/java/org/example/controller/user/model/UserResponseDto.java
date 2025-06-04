package org.example.controller.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "User info")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponseDto extends UserRequestDto {

    @Schema(description = "User ID")
    public Long id;
}
