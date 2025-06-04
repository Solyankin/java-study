package org.example.controller.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "Информация о пользователе")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponseDto extends UserRequestDto {

    @Schema(description = "ID пользователя")
    public Long id;
}
